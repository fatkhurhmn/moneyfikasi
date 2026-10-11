package dev.muffar.moneyfikasi.debt.list

import dev.muffar.moneyfikasi.domain.model.Debt
import dev.muffar.moneyfikasi.domain.model.Wallet

data class DebtsState(
    val debts: List<Debt> = emptyList(),
    val wallets: List<Wallet> = emptyList(),
    val isLoading: Boolean = false
)
