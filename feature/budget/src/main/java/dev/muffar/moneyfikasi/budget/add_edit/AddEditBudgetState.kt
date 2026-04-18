package dev.muffar.moneyfikasi.budget.add_edit

import dev.muffar.moneyfikasi.domain.model.Budget
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.utils.extensions.clearThousandFormat
import java.util.UUID

data class AddEditBudgetState(
    val id: UUID? = null,
    val name: String = "",
    val nameError: ErrorMessage = ErrorMessage(),
    val amount: String = "0",
    val amountError: ErrorMessage = ErrorMessage(),
    val category: Category = Category(),
    val categoryError: ErrorMessage = ErrorMessage(),
    val categoryOptions: List<Category> = emptyList(),
    val showAlert: Boolean = false,
    val budgets: List<Budget> = emptyList()
) {
    val budget: Budget
        get() = Budget(
            id = id ?: UUID.randomUUID(),
            name = name,
            amount = amount.clearThousandFormat().toDoubleOrNull() ?: 0.0,
            category = if (category.name.isNotEmpty()) category else null
        )
}
