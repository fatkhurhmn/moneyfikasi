package dev.muffar.moneyfikasi.transaction.detail

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarType
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.domain.usecase.debt.DebtUseCases
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.constants.ValidationConst
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
class TransactionDetailViewModel @Inject constructor(
    private val transactionUseCases: TransactionUseCases,
    private val debtUseCases: DebtUseCases,
    private val handle: SavedStateHandle,
    private val application: Application
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(TransactionDetailState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: TransactionDetailEvent) {
        when (event) {
            is TransactionDetailEvent.ShowDeleteAlert -> onShowDeleteAlert(event.showAlert)
            is TransactionDetailEvent.DeleteTransaction -> onDeleteTransaction()
            is TransactionDetailEvent.InitData -> onInitData()
            is TransactionDetailEvent.SaveToGallery -> onSaveToGallery(event.bitmap)
            is TransactionDetailEvent.ShowPlusSheet -> onShowPlusSheet(event.show)
            is TransactionDetailEvent.PlusTabSelected -> onPlusTabSelect(event.index)
            is TransactionDetailEvent.PlusAmountChanged -> onPlusAmountChange(event.amount)
            is TransactionDetailEvent.PlusConfirmed -> onPlusConfirm()
            is TransactionDetailEvent.PlusDismissed -> onPlusDismiss()
        }
    }

    private fun onInitData() {
        viewModelScope.launch {
            val isTransfer = handle.get<Boolean>(Screen.TransactionDetail.IS_TRANSFER) ?: false
            val transactionId = handle.get<String>(Screen.TransactionDetail.TRANSACTION_ID)
                ?.let { UUID.fromString(it) } ?: return@launch _eventFlow.emit(UiEvent.NavigateBack)

            if (isTransfer) {
                transactionUseCases.getTransferDetail(transactionId).let {
                    if (it == null) {
                        _eventFlow.emit(UiEvent.NavigateBack)
                        return@let
                    }

                    _state.update { state ->
                        state.copy(
                            transactionId = transactionId,
                            transferDetail = it,
                        )
                    }
                }
            } else {
                transactionUseCases.getTransactionById(transactionId).let {
                    if (it == null) {
                        _eventFlow.emit(UiEvent.NavigateBack)
                        return@let
                    }

                    val linkedDebt = debtUseCases.getDebtByTransactionId(transactionId)
                    _state.update { state ->
                        state.copy(
                            transactionId = transactionId,
                            transaction = it,
                            linkedDebt = linkedDebt,
                        )
                    }
                    linkedDebt?.let { loadHistory(it.id) }
                }
            }
        }
    }

    private fun onShowDeleteAlert(showAlert: Boolean) {
        _state.update { it.copy(showAlert = showAlert) }
    }

    private fun loadHistory(debtId: java.util.UUID) {
        viewModelScope.launch {
            debtUseCases.getDebtHistory(debtId).collectLatest { history ->
                _state.update { it.copy(history = history) }
            }
        }
    }

    private fun onShowPlusSheet(show: Boolean) {
        if (!state.value.canShowPlus && show) return
        _state.update {
            it.copy(
                showPlusSheet = show,
                plusTab = 0,
                plusAmount = "",
                plusAmountError = ErrorMessage()
            )
        }
    }

    private fun onPlusTabSelect(index: Int) {
        _state.update {
            it.copy(
                plusTab = index,
                plusAmount = "",
                plusAmountError = ErrorMessage()
            )
        }
    }

    private fun onPlusDismiss() {
        _state.update {
            it.copy(
                showPlusSheet = false,
                plusAmount = "",
                plusAmountError = ErrorMessage()
            )
        }
    }

    private fun onPlusAmountChange(amount: String) {
        if (amount.length > ValidationConst.MAX_AMOUNT_LENGTH) return
        _state.update { it.copy(plusAmount = amount) }
        updatePlusAmountError()
    }

    private fun updatePlusAmountError() {
        val state = _state.value
        val debt = state.linkedDebt
        val amount = state.plusAmount.clearThousandFormat().toDoubleOrNull() ?: 0.0
        val error = when {
            amount <= 0 -> R.string.error_amount_greater_than_zero
            state.isPlusPaymentTab && debt != null &&
                debt.paidAmount + amount > debt.totalAmount ->
                R.string.error_paid_exceeds_total
            else -> null
        }
        _state.update { it.copy(plusAmountError = ErrorMessage(resId = error)) }
    }

    private fun onPlusConfirm() {
        updatePlusAmountError()
        val state = _state.value
        val debt = state.linkedDebt ?: return
        if (!state.plusAmountError.isNull) return
        val amount = state.plusAmount.clearThousandFormat().toDoubleOrNull() ?: return
        val isPayment = state.isPlusPaymentTab

        viewModelScope.launch {
            _state.update { it.copy(isRecording = true) }
            try {
                if (isPayment) {
                    debtUseCases.recordDebtPayment(debt.id, amount)
                } else {
                    debtUseCases.increaseDebtAmount(debt.id, amount)
                }
                debtUseCases.getDebtById(debt.id)?.let { refreshed ->
                    _state.update { it.copy(linkedDebt = refreshed) }
                }
                _state.update {
                    it.copy(
                        showPlusSheet = false,
                        plusAmount = "",
                        plusAmountError = ErrorMessage(),
                        isRecording = false
                    )
                }
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
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(isRecording = false) }
                val messageResId = when (e.message) {
                    "Paid amount cannot exceed total amount" -> R.string.error_paid_exceeds_total
                    "Amount must be greater than 0" -> R.string.error_amount_greater_than_zero
                    "Debt not found" -> R.string.error_save_debt_failed
                    else -> R.string.error_save_debt_failed
                }
                _eventFlow.emit(UiEvent.ShowMessage(messageResId, SnackbarType.ERROR))
            }
        }
    }

    private fun onDeleteTransaction() {
        state.value.transactionId?.let {
            viewModelScope.launch {
                try {
                    transactionUseCases.deleteTransaction(it)
                    _eventFlow.emit(UiEvent.DeleteTransaction)
                } catch (e: Exception) {
                    e.printStackTrace()
                    _eventFlow.emit(
                        UiEvent.ShowMessage(
                            R.string.error_delete_transaction_failed,
                            SnackbarType.ERROR
                        )
                    )
                }
            }
        }
    }

    private fun onSaveToGallery(bitmap: Bitmap) {
        viewModelScope.launch {
            try {
                transactionUseCases.saveTransactionImage(application, bitmap)
                _eventFlow.emit(UiEvent.ShowMessage(R.string.msg_image_saved, SnackbarType.SUCCESS))
            } catch (e: Exception) {
                e.printStackTrace()
                _eventFlow.emit(UiEvent.ShowMessage(R.string.error_save_image_failed, SnackbarType.ERROR))
            }
        }
    }

    sealed class UiEvent {
        data object DeleteTransaction : UiEvent()
        data class ShowMessage(val messageResId: Int, val type: SnackbarType) : UiEvent()
        data object NavigateBack : UiEvent()
    }
}
