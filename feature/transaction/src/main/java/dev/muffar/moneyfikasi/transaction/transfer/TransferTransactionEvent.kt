package dev.muffar.moneyfikasi.transaction.transfer

import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.transaction.transfer.component.TransferTransactionSheetType

sealed class TransferTransactionEvent {
    data class AmountChanged(val amount: String) : TransferTransactionEvent()
    data class FeeChanged(val fee: String) : TransferTransactionEvent()
    data class SourceWalletSelected(val wallet: Wallet) : TransferTransactionEvent()
    data class TargetWalletSelected(val wallet: Wallet) : TransferTransactionEvent()
    data class DateSelected(val date: Long) : TransferTransactionEvent()
    data class TimeSelected(val hour: Int, val minute: Int) : TransferTransactionEvent()
    data class NoteChanged(val note: String) : TransferTransactionEvent()
    data object SaveTransfer : TransferTransactionEvent()
    data class OnBottomSheetChange(val type: TransferTransactionSheetType?) : TransferTransactionEvent()
}