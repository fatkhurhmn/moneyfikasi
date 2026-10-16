package dev.muffar.moneyfikasi.transaction.detail.plus

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarType
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.usecase.debt.DebtUseCases
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.constants.UUIDConst
import dev.muffar.moneyfikasi.utils.constants.ValidationConst
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.toMilliseconds
import dev.muffar.moneyfikasi.utils.extensions.LongExt.toLocalDateTime
import dev.muffar.moneyfikasi.utils.extensions.StringExt.clearThousandFormat
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DebtPlusViewModel @Inject constructor(
    private val debtUseCases: DebtUseCases,
    private val transactionUseCases: TransactionUseCases,
    private val walletUseCases: WalletUseCases,
    private val handle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(DebtPlusState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        initState()
        loadWallets()
    }

    fun onEvent(event: DebtPlusEvent) {
        when (event) {
            is DebtPlusEvent.TabSelected -> onTabSelect(event.index)
            is DebtPlusEvent.AmountChanged -> onAmountChange(event.amount)
            is DebtPlusEvent.WalletSelected -> onWalletSelect(event.wallet)
            is DebtPlusEvent.DateSelected -> onDateSelect(event.date)
            is DebtPlusEvent.SavePlus -> onSavePlus()
        }
    }

    private fun initState() {
        val debtId = handle.get<String>(Screen.DebtPlus.DEBT_ID)
            ?.takeIf { it.isNotEmpty() }
            ?.let { UUID.fromString(it) }
        val tab = handle.get<Int>(Screen.DebtPlus.TAB) ?: 0
        val historyId = handle.get<String>(Screen.DebtPlus.HISTORY_ID)
            ?.takeIf { it.isNotEmpty() }
            ?.let { UUID.fromString(it) }
        if (debtId == null) {
            viewModelScope.launch { _eventFlow.emit(UiEvent.NavigateBack) }
            return
        }
        _state.update { it.copy(debtId = debtId, tab = tab, editingHistoryId = historyId) }
        viewModelScope.launch {
            val debt = debtUseCases.getDebtById(debtId)
            if (debt == null) {
                _eventFlow.emit(UiEvent.NavigateBack)
                return@launch
            }
            _state.update { it.copy(debt = debt) }
            if (historyId != null) {
                _state.update { it.copy(editingHistoryId = historyId) }
                loadHistoryRow(historyId)
            } else {
                loadWallet(debt.walletId)
            }
        }
    }

    private fun loadHistoryRow(historyId: UUID) {
        viewModelScope.launch {
            val debt = _state.value.debt
            val row = transactionUseCases.getTransactionById(historyId)
            if (debt == null || row == null) {
                _eventFlow.emit(UiEvent.NavigateBack)
                return@launch
            }
            val isPayment = row.type != debt.type.toTransactionType()
            val tab = if (debt.type == DebtType.DEBT) {
                if (isPayment) 1 else 0
            } else {
                if (isPayment) 0 else 1
            }
            _state.update {
                it.copy(
                    tab = tab,
                    amount = row.amount.formatThousand(),
                    date = row.date.toMilliseconds(),
                    wallet = row.wallet
                )
            }
        }
    }

    private fun loadWallets() {
        viewModelScope.launch {
            walletUseCases.getAllWallets().collectLatest { wallets ->
                _state.update { it.copy(walletOptions = wallets.filter { it.isActive }) }
            }
        }
    }

    private fun loadWallet(walletId: UUID) {
        viewModelScope.launch {
            val wallet = if (walletId == UUIDConst.empty) {
                null
            } else {
                walletUseCases.getWalletById(walletId)
            }
            _state.update {
                it.copy(wallet = wallet ?: Wallet(id = UUIDConst.empty, name = "Unknown"))
            }
        }
    }

    private fun onTabSelect(index: Int) {
        _state.update { it.copy(tab = index) }
        updateAmountError()
    }

    private fun onAmountChange(amount: String) {
        if (amount.length > ValidationConst.MAX_AMOUNT_LENGTH) return
        _state.update { it.copy(amount = amount) }
        updateAmountError()
    }

    private fun onWalletSelect(wallet: Wallet) {
        _state.update { it.copy(wallet = wallet) }
        updateWalletError()
    }

    private fun onDateSelect(date: Long) {
        _state.update { it.copy(date = date) }
    }

    private fun updateAmountError() {
        val amount = _state.value.amount.clearThousandFormat().toDoubleOrNull() ?: 0.0
        val error = if (amount <= 0) R.string.error_amount_greater_than_zero else null
        _state.update { it.copy(amountError = ErrorMessage(resId = error)) }
    }

    private fun updateWalletError() {
        val wallet = _state.value.wallet
        val error = if (wallet.isNotSet) R.string.error_select_wallet else null
        _state.update { it.copy(walletError = ErrorMessage(resId = error)) }
    }

    private fun onSavePlus() {
        if (_state.value.isRecording) return
        updateAmountError()
        updateWalletError()
        val state = _state.value
        val debt = state.debt ?: return
        if (!state.amountError.isNull || !state.walletError.isNull) return
        val amount = state.amount.clearThousandFormat().toDoubleOrNull() ?: return
        val isPayment = state.isPaymentTab
        val historyId = state.editingHistoryId

        viewModelScope.launch {
            _state.update { it.copy(isRecording = true) }
            try {
                if (historyId != null) {
                    debtUseCases.updateDebtHistory(
                        debtId = debt.id,
                        transactionId = historyId,
                        amount = amount,
                        date = state.date.toLocalDateTime(),
                        walletId = state.wallet.id,
                        note = debt.name,
                        isPayment = isPayment
                    )
                } else if (isPayment) {
                    debtUseCases.recordDebtPayment(
                        debtId = debt.id,
                        amount = amount,
                        walletId = state.wallet.id,
                        date = state.date.toLocalDateTime()
                    )
                } else {
                    debtUseCases.increaseDebtAmount(
                        debtId = debt.id,
                        amount = amount,
                        walletId = state.wallet.id,
                        date = state.date.toLocalDateTime()
                    )
                }
                _state.update { it.copy(isRecording = false) }
                _eventFlow.emit(
                    UiEvent.ShowMessage(
                        if (isPayment) {
                            R.string.msg_payment_success
                        } else {
                            R.string.msg_debt_updated
                        },
                        SnackbarType.SUCCESS
                    )
                )
                _eventFlow.emit(UiEvent.SaveDebtPlus)
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(isRecording = false) }
                val messageResId = when (e.message) {
                    "Total cannot be less than paid amount" -> R.string.error_total_below_paid
                    "Amount must be greater than 0" -> R.string.error_amount_greater_than_zero
                    "Debt not found", "Transaction not found" -> R.string.error_save_debt_failed
                    else -> R.string.error_save_debt_failed
                }
                _eventFlow.emit(UiEvent.ShowMessage(messageResId, SnackbarType.ERROR))
            }
        }
    }

    sealed class UiEvent {
        data class ShowMessage(val messageResId: Int, val type: SnackbarType) : UiEvent()
        data object SaveDebtPlus : UiEvent()
        data object NavigateBack : UiEvent()
    }
}
