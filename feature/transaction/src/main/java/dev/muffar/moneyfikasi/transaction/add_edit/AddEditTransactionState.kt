package dev.muffar.moneyfikasi.transaction.add_edit

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.AmountInputType
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.utils.extensions.LongExt.format
import dev.muffar.moneyfikasi.utils.extensions.LongExt.formattedDate
import java.util.UUID

data class AddEditTransactionState(
    val id: UUID? = null,
    val type: TransactionType = TransactionType.EXPENSE,

    val amount: String = "0",
    val amountError: ErrorMessage = ErrorMessage(),

    val category: Category = Category(),
    val categoryError: ErrorMessage = ErrorMessage(),

    val wallet: Wallet = Wallet(),
    val walletError: ErrorMessage = ErrorMessage(),

    val date: Long = System.currentTimeMillis(),

    val time: Long = System.currentTimeMillis(),
    val hour: Int = time.format("H").toInt(),
    val minute: Int = time.format("mm").toInt(),

    val note: String = "",

    val categoryOptions: List<Category> = emptyList(),
    val walletOptions: List<Wallet> = emptyList(),
    val amountInputType: AmountInputType = AmountInputType.CALCULATOR,
) {
    val isEditMode: Boolean
        get() = id != null

    val categoryType: CategoryType
        get() = when (type) {
            TransactionType.INCOME, TransactionType.TRANSFER_IN -> CategoryType.INCOME
            else -> CategoryType.EXPENSE
        }
}
