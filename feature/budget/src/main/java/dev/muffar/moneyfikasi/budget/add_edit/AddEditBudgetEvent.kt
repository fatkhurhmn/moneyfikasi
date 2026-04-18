package dev.muffar.moneyfikasi.budget.add_edit

import dev.muffar.moneyfikasi.domain.model.Category

sealed class AddEditBudgetEvent {
    data class OnAmountChange(val amount: String) : AddEditBudgetEvent()
    data class OnCategoryChange(val category: Category?) : AddEditBudgetEvent()
    data object OnSaveBudget : AddEditBudgetEvent()
    data object OnDeleteBudget : AddEditBudgetEvent()
    data class OnShowDeleteAlert(val show: Boolean) : AddEditBudgetEvent()
}
