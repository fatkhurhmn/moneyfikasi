package dev.muffar.moneyfikasi.transaction.transfer

import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.transaction.transfer.component.TransferTransactionSheetType

sealed class TransferTransactionEvent {
    data class OnAmountChange(val amount: String) : TransferTransactionEvent()
    data class OnFeeChange(val fee: String) : TransferTransactionEvent()
    data class OnSourceWalletSelect(val wallet: Wallet) : TransferTransactionEvent()
    data class OnTargetWalletSelect(val wallet: Wallet) : TransferTransactionEvent()
    data class OnDateSelect(val date: Long) : TransferTransactionEvent()
    data class OnTimeSelect(val hour: Int, val minute: Int) : TransferTransactionEvent()
    data class OnNoteChange(val note: String) : TransferTransactionEvent()
    data object OnCreateClicked : TransferTransactionEvent()
    data class OnBottomSheetChange(val type: TransferTransactionSheetType?) : TransferTransactionEvent()
}