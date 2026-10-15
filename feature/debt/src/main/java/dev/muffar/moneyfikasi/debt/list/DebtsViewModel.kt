package dev.muffar.moneyfikasi.debt.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarType
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.domain.usecase.debt.DebtUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.constants.ValidationConst
import dev.muffar.moneyfikasi.utils.extensions.StringExt.clearThousandFormat
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DebtsViewModel @Inject constructor(
    private val debtUseCases: DebtUseCases,
    private val walletUseCases: WalletUseCases,
    private val handle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(DebtsState())
    val state: StateFlow<DebtsState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        initType()
        loadDebts()
        loadWallets()
    }

    fun onEvent(event: DebtsEvent) {
        when (event) {
            is DebtsEvent.StatusFilterChanged -> onStatusFilterChange(event.filter)
            is DebtsEvent.PaymentOpened -> onPaymentOpen(event.debtId)
            is DebtsEvent.PaymentDismissed -> onPaymentDismiss()
            is DebtsEvent.PaymentAmountChanged -> onPaymentAmountChange(event.amount)
            is DebtsEvent.PaymentConfirmed -> onPaymentConfirm()
        }
    }

    private fun initType() {
        handle.get<String>(Screen.Debts.TYPE)?.let { typeStr ->
            if (typeStr.isNotEmpty()) {
                _state.update { it.copy(selectedType = DebtType.fromString(typeStr)) }
            }
        }
    }

    private fun loadDebts() {
        viewModelScope.launch {
            debtUseCases.getAllDebts().collectLatest { debts ->
                _state.update { it.copy(debts = debts) }
            }
        }
    }

    private fun loadWallets() {
        viewModelScope.launch {
            walletUseCases.getAllWallets().collectLatest { wallets ->
                _state.update { it.copy(wallets = wallets) }
            }
        }
    }

    private fun onStatusFilterChange(filter: DebtStatusFilter) {
        _state.update { it.copy(statusFilter = filter) }
    }

    private fun onPaymentOpen(debtId: UUID) {
        _state.update {
            it.copy(
                paymentDebtId = debtId,
                paymentAmount = "",
                paymentAmountError = ErrorMessage()
            )
        }
    }

    private fun onPaymentDismiss() {
        _state.update {
            it.copy(
                paymentDebtId = null,
                paymentAmount = "",
                paymentAmountError = ErrorMessage()
            )
        }
    }

    private fun onPaymentAmountChange(amount: String) {
        if (amount.length > ValidationConst.MAX_AMOUNT_LENGTH) return
        _state.update { it.copy(paymentAmount = amount) }
        updatePaymentAmountError()
    }

    private fun updatePaymentAmountError() {
        val state = _state.value
        val debt = state.paymentDebt
        val amount = state.paymentAmount.clearThousandFormat().toDoubleOrNull() ?: 0.0
        val error = when {
            amount <= 0 -> R.string.error_amount_greater_than_zero
            debt != null && debt.paidAmount + amount > debt.totalAmount ->
                R.string.error_paid_exceeds_total
            else -> null
        }
        _state.update { it.copy(paymentAmountError = ErrorMessage(resId = error)) }
    }

    private fun onPaymentConfirm() {
        updatePaymentAmountError()
        val state = _state.value
        val debt = state.paymentDebt ?: return
        if (!state.paymentAmountError.isNull) return
        val amount = state.paymentAmount.clearThousandFormat().toDoubleOrNull() ?: return

        viewModelScope.launch {
            _state.update { it.copy(isRecordingPayment = true) }
            try {
                debtUseCases.recordDebtPayment(debt.id, amount)
                _state.update {
                    it.copy(
                        paymentDebtId = null,
                        paymentAmount = "",
                        paymentAmountError = ErrorMessage(),
                        isRecordingPayment = false
                    )
                }
                _eventFlow.emit(
                    UiEvent.ShowMessage(R.string.msg_payment_success, SnackbarType.SUCCESS)
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(isRecordingPayment = false) }
                val messageResId = when (e.message) {
                    "Paid amount cannot exceed total amount" -> R.string.error_paid_exceeds_total
                    "Amount must be greater than 0" -> R.string.error_amount_greater_than_zero
                    else -> R.string.error_record_payment_failed
                }
                _eventFlow.emit(UiEvent.ShowMessage(messageResId, SnackbarType.ERROR))
            }
        }
    }

    sealed class UiEvent {
        data class ShowMessage(val messageResId: Int, val type: SnackbarType) : UiEvent()
    }
}
