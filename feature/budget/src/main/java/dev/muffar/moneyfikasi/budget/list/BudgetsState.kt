package dev.muffar.moneyfikasi.budget.list

import dev.muffar.moneyfikasi.domain.model.Budget
import dev.muffar.moneyfikasi.domain.model.Wallet

data class BudgetsState(
    val wallets: List<Wallet> = emptyList(),
    val budgets: List<Budget> = emptyList(),
    val budgetCutoffDay: Int = 1,
    val periodStart: Long = 0L,
    val periodEnd: Long = 0L,
    val isLoading: Boolean = false
)
