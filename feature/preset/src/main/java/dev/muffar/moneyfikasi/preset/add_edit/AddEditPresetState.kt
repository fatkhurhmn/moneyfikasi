package dev.muffar.moneyfikasi.preset.add_edit

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.domain.model.Preset
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.utils.constants.UUIDConst
import dev.muffar.moneyfikasi.utils.extensions.StringExt.clearThousandFormat
import java.util.UUID

data class AddEditPresetState(
    val id: UUID? = null,
    val name: String = "",
    val amount: String = "0",
    val type: TransactionType = TransactionType.EXPENSE,
    val category: Category = Category(),
    val wallet: Wallet = Wallet(),
    val nameError: ErrorMessage = ErrorMessage(),
    val categories: List<Category> = emptyList(),
    val wallets: List<Wallet> = emptyList(),
    val isLoading: Boolean = false,
    val showAlert: Boolean = false,
) {
    val categoryType: CategoryType
        get() = when (type) {
            TransactionType.INCOME, TransactionType.TRANSFER_IN -> CategoryType.INCOME
            else -> CategoryType.EXPENSE
        }

    val preset: Preset
        get() = Preset(
            id = id ?: UUID.randomUUID(),
            name = name.trim(),
            amount = amount.clearThousandFormat().toDoubleOrNull(),
            type = type,
            category = if (category.id == UUIDConst.empty) null else category,
            wallet = if (wallet.id == UUIDConst.empty) null else wallet
        )
}
