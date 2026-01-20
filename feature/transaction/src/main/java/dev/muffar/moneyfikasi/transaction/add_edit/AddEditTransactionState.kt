package dev.muffar.moneyfikasi.transaction.add_edit

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.transaction.add_edit.component.AddEditTransactionSheetType
import dev.muffar.moneyfikasi.utils.extensions.toFormattedDateTime
import java.util.UUID

data class AddEditTransactionState(
    val id: UUID? = null,
    val type: TransactionType = TransactionType.EXPENSE,
    val amount: String = "0",
    val adminFee: String = "0",
    val category: Category = Category(),
    val wallet: Wallet = Wallet(),
    val originalWallet: Wallet = Wallet(),
    val destinationWallet: Wallet = Wallet(),
    val date: Long = System.currentTimeMillis(),
    val time: Long = System.currentTimeMillis(),
    val hour: Int = time.toFormattedDateTime("H").toInt(),
    val minute: Int = time.toFormattedDateTime("mm").toInt(),
    val note: String = "",
    val categories: List<Category> = emptyList(),
    val walletOptions: List<Wallet> = emptyList(),
    val bottomSheetType: AddEditTransactionSheetType? = null,
) {
    val isEditMode: Boolean
        get() = id != null
}