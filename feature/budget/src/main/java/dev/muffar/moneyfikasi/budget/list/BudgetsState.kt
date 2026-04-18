package dev.muffar.moneyfikasi.budget.list

import dev.muffar.moneyfikasi.domain.model.Budget
import dev.muffar.moneyfikasi.domain.model.Wallet

data class BudgetsState(
    val wallets: List<Wallet> = emptyList(),
    val budgets: List<Budget> = emptyList(),
    val isLoading: Boolean = false
)
