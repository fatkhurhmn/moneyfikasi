package dev.muffar.moneyfikasi.debt.list

import dev.muffar.moneyfikasi.domain.model.Debt

data class DebtsState(
    val debts: List<Debt> = emptyList(),
    val isLoading: Boolean = false
)
