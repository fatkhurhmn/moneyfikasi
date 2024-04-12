package dev.muffar.moneyfikasi.transaction.add_edit

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.transaction.add_edit.component.AddEditTransactionSheetType
import dev.muffar.moneyfikasi.utils.toFormattedDateTime
import java.util.UUID

data class AddEditTransactionState(
    val id: UUID? = null,
    val type: TransactionType = TransactionType.EXPENSE,
    val amount: String = "0",
    val categoryId: UUID? = null,
    val categoryName: String = "",
    val categoryIcon: String = "",
    val categoryColor: Long = 0,
    val walletId: UUID? = null,
    val walletName: String = "",
    val walletIcon: String = "",
    val walletColor: Long = 0,
    val date: Long = System.currentTimeMillis(),
    val time: Long = System.currentTimeMillis(),
    val hour: Int = time.toFormattedDateTime("HH").toInt(),
    val minute: Int = time.toFormattedDateTime("mm").toInt(),
    val description: String = "",
    val categories: List<Category> = emptyList(),
    val wallets: List<Wallet> = emptyList(),
    val bottomSheetType: AddEditTransactionSheetType? = null,
)