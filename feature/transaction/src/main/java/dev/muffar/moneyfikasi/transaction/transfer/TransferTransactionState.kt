package dev.muffar.moneyfikasi.transaction.transfer

import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.utils.extensions.LongExt.toFormattedDateTime
import java.util.UUID

data class TransferTransactionState(
    val id: UUID? = null,

    val amount: String = "0",
    val amountError: ErrorMessage = ErrorMessage(),

    val fee: String = "0",
    val note: String = "",

    val sourceWallet: Wallet = Wallet(),
    val sourceWalletError: ErrorMessage = ErrorMessage(),

    val targetWallet: Wallet = Wallet(),
    val targetWalletError: ErrorMessage = ErrorMessage(),

    val date: Long = System.currentTimeMillis(),
    val time: Long = System.currentTimeMillis(),
    val hour: Int = time.toFormattedDateTime("H").toInt(),
    val minute: Int = time.toFormattedDateTime("mm").toInt(),
    val walletOptions: List<Wallet> = emptyList(),
) {
    val isEditMode: Boolean
        get() = id != null
}
