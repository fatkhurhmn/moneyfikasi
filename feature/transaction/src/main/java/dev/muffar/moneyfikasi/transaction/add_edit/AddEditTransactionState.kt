package dev.muffar.moneyfikasi.transaction.add_edit

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.transaction.add_edit.component.AddEditTransactionSheetType
import dev.muffar.moneyfikasi.utils.toEmptyUUID
import dev.muffar.moneyfikasi.utils.toFormattedDateTime
import java.util.UUID

data class AddEditTransactionState(
    val id: UUID? = null,
    val type: TransactionType = TransactionType.EXPENSE,
    val amount: String = "0",
    val category :Category = Category(
        id = UUID.fromString("".toEmptyUUID()),
        name = "",
        icon = "",
        color = 0,
        type = CategoryType.INCOME,
    ),
    val wallet: Wallet = Wallet(
        id = UUID.fromString("".toEmptyUUID()),
        name = "",
        icon = "",
        color = 0,
        balance = 0.0,
    ),
    val date: Long = System.currentTimeMillis(),
    val time: Long = System.currentTimeMillis(),
    val hour: Int = time.toFormattedDateTime("HH").toInt(),
    val minute: Int = time.toFormattedDateTime("mm").toInt(),
    val description: String = "",
    val categories: List<Category> = emptyList(),
    val wallets: List<Wallet> = emptyList(),
    val bottomSheetType: AddEditTransactionSheetType? = null,
)