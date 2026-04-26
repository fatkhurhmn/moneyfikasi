package dev.muffar.moneyfikasi.budget.add_edit

import dev.muffar.moneyfikasi.domain.model.Category

sealed class AddEditBudgetEvent {
    data class AmountChanged(val amount: String) : AddEditBudgetEvent()
    data class CategoryChanged(val category: Category?) : AddEditBudgetEvent()
    data object SaveBudget : AddEditBudgetEvent()
    data object DeleteBudget : AddEditBudgetEvent()
    data class ShowDeleteAlert(val show: Boolean) : AddEditBudgetEvent()
}
