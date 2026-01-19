package dev.muffar.moneyfikasi.transaction.transfer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.InvalidTransactionException
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.transaction.transfer.component.TransferTransactionSheetType
import dev.muffar.moneyfikasi.utils.extensions.clearThousandFormat
import dev.muffar.moneyfikasi.utils.extensions.formatThousand
import dev.muffar.moneyfikasi.utils.extensions.toFormattedDateTime
import dev.muffar.moneyfikasi.utils.extensions.toMilliseconds
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
            is TransferTransactionEvent.OnAmountChange -> onAmountChange(event.amount)
            is TransferTransactionEvent.OnFeeChange -> onFeeChange(event.fee)
            is TransferTransactionEvent.OnSourceWalletSelect -> onSourceWalletSelect(event.wallet)
            is TransferTransactionEvent.OnTargetWalletSelect -> onTargetWalletSelect(event.wallet)
            is TransferTransactionEvent.OnDateSelect -> onDateSelect(event.date)
            is TransferTransactionEvent.OnTimeSelect -> onTimeSelect(event.hour, event.minute)
            is TransferTransactionEvent.OnNoteChange -> onNoteChange(event.note)
            is TransferTransactionEvent.OnCreateClicked -> onSaveTransaction()
            is TransferTransactionEvent.OnBottomSheetChange -> onBottomSheetChange(event.type)
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
                _state.update { it.copy(wallets = activeWallets) }
            }
        }
    }

    private fun onAmountChange(amount: String) {
        if (amount.length > 17) return
        _state.update { it.copy(amount = amount) }
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
    }

    private fun onTargetWalletSelect(wallet: Wallet) {
        _state.update {
            it.copy(targetWallet = wallet)
        }
    }

    private fun onDateSelect(date: Long) {
        _state.update { it.copy(date = date) }
    }

    private fun onTimeSelect(hour: Int, minute: Int) {
        _state.update { it.copy(hour = hour, minute = minute) }
    }

    private fun onSaveTransaction() {
        val state = this.state.value
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
            } catch (e: InvalidTransactionException) {
                _eventFlow.emit(UiEvent.ShowMessage(e.message))
            }
        }
    }

    private fun onBottomSheetChange(type: TransferTransactionSheetType?) {
        _state.update { it.copy(bottomSheetType = type) }
    }

    private fun getFormattedDate(): LocalDateTime {
        return LocalDateTime
            .ofInstant(Instant.ofEpochMilli(state.value.date), ZoneId.systemDefault())
            .withHour(state.value.hour)
            .withMinute(state.value.minute)
    }

    sealed class UiEvent {
        data class ShowMessage(val message: String) : UiEvent()
        data object SaveTransaction : UiEvent()
        data object DeleteTransaction : UiEvent()
    }
}