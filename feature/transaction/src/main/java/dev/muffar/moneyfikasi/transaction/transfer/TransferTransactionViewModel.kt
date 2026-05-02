package dev.muffar.moneyfikasi.transaction.transfer

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarType
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.utils.extensions.StringExt.clearThousandFormat
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand
import dev.muffar.moneyfikasi.utils.extensions.LongExt.toFormattedDateTime
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.toMilliseconds
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TransferTransactionViewModel @Inject constructor(
    private val transactionUseCases: TransactionUseCases,
    private val walletUseCases: WalletUseCases,
    private val handle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(TransferTransactionState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        initState()
        loadWallets()
    }

    fun onEvent(event: TransferTransactionEvent) {
        when (event) {
            is TransferTransactionEvent.AmountChanged -> onAmountChange(event.amount)
            is TransferTransactionEvent.FeeChanged -> onFeeChange(event.fee)
            is TransferTransactionEvent.SourceWalletSelected -> onSourceWalletSelect(event.wallet)
            is TransferTransactionEvent.TargetWalletSelected -> onTargetWalletSelect(event.wallet)
            is TransferTransactionEvent.DateSelected -> onDateSelect(event.date)
            is TransferTransactionEvent.TimeSelected -> onTimeSelect(event.time)
            is TransferTransactionEvent.NoteChanged -> onNoteChange(event.note)
            is TransferTransactionEvent.SaveTransfer -> onSaveTransfer()
        }
    }

    private fun initState() {
        handle.get<String>(Screen.TransferTransaction.TRANSACTION_ID)?.let { id ->
            if (id.isEmpty()) return
            val transactionId = UUID.fromString(id)
            viewModelScope.launch {
                transactionUseCases.getTransferDetail(transactionId)?.let { detail ->
                    _state.update { state ->
                        val date = detail.date.toMilliseconds()
                        with(detail) {
                            state.copy(
                                id = referenceId,
                                amount = amount.formatThousand(),
                                sourceWallet = sourceWallet,
                                targetWallet = targetWallet,
                                fee = fee.formatThousand(),
                                note = note ?: "",
                                date = date,
                                hour = date.toFormattedDateTime("H").toInt(),
                                minute = date.toFormattedDateTime("mm").toInt()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadWallets() {
        viewModelScope.launch {
            walletUseCases.getAllWallets().collectLatest { wallets ->
                val activeWallets = wallets.filter { it.isActive }
                _state.update { it.copy(walletOptions = activeWallets) }
            }
        }
    }

    private fun onAmountChange(amount: String) {
        if (amount.length > 17) return
        _state.update { it.copy(amount = amount) }
        updateAmountError()
    }

    private fun updateAmountError() {
        val amount = state.value.amount
        val error = when {
            amount.isEmpty() -> "Amount cannot be empty"
            amount.clearThousandFormat().toDouble() == 0.0 -> "Minimum amount is 1"
            else -> null
        }
        _state.update { it.copy(amountError = ErrorMessage(error)) }
    }

    private fun onFeeChange(fee: String) {
        if (fee.length > 17) return
        _state.update { it.copy(fee = fee) }
    }

    private fun onNoteChange(note: String) {
        if (note.length > 255) return
        _state.update { it.copy(note = note) }
    }

    private fun onSourceWalletSelect(wallet: Wallet) {
        _state.update {
            it.copy(sourceWallet = wallet)
        }
        updateSourceWalletError()
    }

    private fun updateSourceWalletError() {
        val wallet = state.value.sourceWallet
        val error = if (wallet.isNotSet) "Source wallet cannot be empty" else null
        _state.update { it.copy(sourceWalletError = ErrorMessage(error)) }
    }

    private fun onTargetWalletSelect(wallet: Wallet) {
        _state.update {
            it.copy(targetWallet = wallet)
        }
        updateTargetWalletError()
    }

    private fun updateTargetWalletError() {
        val wallet = state.value.targetWallet
        val error = if (wallet.isNotSet) "Target wallet cannot be empty" else null
        _state.update { it.copy(targetWalletError = ErrorMessage(error)) }
    }

    private fun onDateSelect(date: Long) {
        _state.update { it.copy(date = date) }
    }

    private fun onTimeSelect(time: Pair<Int, Int>) {
        _state.update { it.copy(hour = time.first, minute = time.second) }
    }

    private fun onSaveTransfer() {
        val state = this.state.value
        if (!isFormValid()) return
        viewModelScope.launch {
            try {
                if (state.isEditMode) {
                    transactionUseCases.updateTransfer(
                        referenceId = state.id!!,
                        sourceWalletId = state.sourceWallet.id,
                        targetWalletId = state.targetWallet.id,
                        amount = state.amount.clearThousandFormat().toDouble(),
                        fee = state.fee.clearThousandFormat().toDouble(),
                        date = getFormattedDate(),
                        note = "",
                    )

                } else {
                    transactionUseCases.addTransfer(
                        sourceWalletId = state.sourceWallet.id,
                        targetWalletId = state.targetWallet.id,
                        amount = state.amount.clearThousandFormat().toDouble(),
                        fee = state.fee.clearThousandFormat().toDouble(),
                        date = getFormattedDate(),
                        note = "",
                    )
                }
                _eventFlow.emit(UiEvent.SaveTransaction)
            } catch (e: Exception) {
                Log.e("TransferTransactionViewModel", "Error saving transaction: ${e.message}")
                _eventFlow.emit(UiEvent.ShowMessage(e.message ?: "", SnackbarType.ERROR))
            }
        }
    }

    private fun isFormValid(): Boolean {
        viewModelScope.launch {
            updateAmountError()
            updateSourceWalletError()
            updateTargetWalletError()
        }
        return state.value.run {
            amountError.isNull && sourceWalletError.isNull && targetWalletError.isNull
        }
    }

    private fun getFormattedDate(): LocalDateTime {
        return LocalDateTime
            .ofInstant(Instant.ofEpochMilli(state.value.date), ZoneId.systemDefault())
            .withHour(state.value.hour)
            .withMinute(state.value.minute)
    }

    sealed class UiEvent {
        data class ShowMessage(val message: String, val type: SnackbarType) : UiEvent()
        data object SaveTransaction : UiEvent()
        data object DeleteTransaction : UiEvent()
    }
}