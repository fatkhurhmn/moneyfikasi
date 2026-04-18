package dev.muffar.moneyfikasi.budget.list

import dev.muffar.moneyfikasi.domain.model.Budget

data class BudgetsState(
    val budgets: List<Budget> = emptyList(),
    val isLoading: Boolean = false
)
