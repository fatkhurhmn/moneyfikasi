package dev.muffar.moneyfikasi.transaction.transfer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarType
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.usecase.preferences.ui.UiSettingsUseCases
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.toMilliseconds
import dev.muffar.moneyfikasi.utils.extensions.LongExt.format
import dev.muffar.moneyfikasi.utils.extensions.StringExt.clearThousandFormat
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
    private val uiSettingsUseCases: UiSettingsUseCases,
    private val handle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(TransferTransactionState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        initState()
        loadWallets()
        loadUiSettings()
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
        val transactionIdStr = handle.get<String>(Screen.TransferTransaction.TRANSACTION_ID)
        val amount = handle.get<String>(Screen.TransferTransaction.AMOUNT)
        val note = handle.get<String>(Screen.TransferTransaction.NOTE)
        val fromWalletName = handle.get<String>(Screen.TransferTransaction.FROM_WALLET)
        val toWalletName = handle.get<String>(Screen.TransferTransaction.TO_WALLET)

        if (!transactionIdStr.isNullOrEmpty()) {
            val transactionId = UUID.fromString(transactionIdStr)
            viewModelScope.launch {
                transactionUseCases.getTransferDetail(transactionId)?.let { detail ->
                    _state.update { state ->
                        val date = detail.date.toMilliseconds()
                        state.copy(
                            id = detail.referenceId,
                            amount = detail.amount.formatThousand(),
                            sourceWallet = detail.sourceWallet,
                            targetWallet = detail.targetWallet,
                            fee = detail.fee.formatThousand(),
                            note = detail.note ?: "",
                            date = date,
                            hour = date.format("H").toInt(),
                            minute = date.format("mm").toInt()
                        )
                    }
                }
            }
        } else {
            _state.update {
                it.copy(
                    amount = amount ?: "0",
                    note = note ?: ""
                )
            }
            if (!fromWalletName.isNullOrEmpty() || !toWalletName.isNullOrEmpty()) {
                viewModelScope.launch {
                    walletUseCases.getAllWallets().collectLatest { wallets ->
                        val fromWallet = wallets.find { it.name.equals(fromWalletName, true) }
                        val toWallet = wallets.find { it.name.equals(toWalletName, true) }
                        _state.update {
                            it.copy(
                                sourceWallet = fromWallet ?: it.sourceWallet,
                                targetWallet = toWallet ?: it.targetWallet
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

    private fun loadUiSettings() {
        viewModelScope.launch {
            uiSettingsUseCases.getUiSettings().collectLatest { settings ->
                _state.update { it.copy(amountInputType = settings.amountInputType) }
            }
        }
    }

    private fun onAmountChange(amount: String) {
        _state.update { it.copy(amount = amount) }
        updateAmountError()
    }

    private fun updateAmountError() {
        val amount = state.value.amount
        val error = when {
            amount.isEmpty() -> R.string.error_amount_empty
            amount.clearThousandFormat().toDouble() == 0.0 -> R.string.error_amount_greater_than_zero
            else -> null
        }
        _state.update { it.copy(amountError = ErrorMessage(resId = error)) }
    }

    private fun onFeeChange(fee: String) {
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
        val error = if (wallet.isNotSet) R.string.error_source_wallet_empty else null
        _state.update { it.copy(sourceWalletError = ErrorMessage(resId = error)) }
    }

    private fun onTargetWalletSelect(wallet: Wallet) {
        _state.update {
            it.copy(targetWallet = wallet)
        }
        updateTargetWalletError()
    }

    private fun updateTargetWalletError() {
        val wallet = state.value.targetWallet
        val error = if (wallet.isNotSet) R.string.error_target_wallet_empty else null
        _state.update { it.copy(targetWalletError = ErrorMessage(resId = error)) }
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
