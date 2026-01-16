package dev.muffar.moneyfikasi.transaction.transfer

import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.transaction.transfer.component.TransferTransactionSheetType
import dev.muffar.moneyfikasi.utils.toFormattedDateTime
import java.util.UUID

data class TransferTransactionState(
    val id: UUID? = null,
    val amount: String = "0",
    val fee: String = "0",
    val note: String = "",
    val sourceWallet: Wallet = Wallet(),
    val targetWallet: Wallet = Wallet(),
    val date: Long = System.currentTimeMillis(),
    val time: Long = System.currentTimeMillis(),
    val hour: Int = time.toFormattedDateTime("H").toInt(),
    val minute: Int = time.toFormattedDateTime("mm").toInt(),
    val wallets: List<Wallet> = emptyList(),
    val bottomSheetType: TransferTransactionSheetType? = null,
) {
    val isEditMode: Boolean
        get() = id != null
}
