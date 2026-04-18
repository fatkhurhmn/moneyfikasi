package dev.muffar.moneyfikasi.budget.list

import dev.muffar.moneyfikasi.domain.model.Budget

sealed class BudgetsEvent {
    data class DeleteBudget(val budget: Budget) : BudgetsEvent()
}
