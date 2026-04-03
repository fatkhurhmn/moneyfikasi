package dev.muffar.moneyfikasi.preset.add_edit

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet
import java.util.UUID

data class AddEditPresetState(
    val id: UUID? = null,
    val name: String = "",
    val amount: String = "0",
    val type: TransactionType = TransactionType.EXPENSE,
    val category: Category = Category(),
    val wallet: Wallet = Wallet(),
    val note: String = "",
    val nameError: ErrorMessage = ErrorMessage(),
    val categories: List<Category> = emptyList(),
    val wallets: List<Wallet> = emptyList(),
    val isLoading: Boolean = false,
)
