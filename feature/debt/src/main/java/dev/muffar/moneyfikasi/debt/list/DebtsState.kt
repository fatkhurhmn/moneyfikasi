package dev.muffar.moneyfikasi.debt.list

import dev.muffar.moneyfikasi.domain.model.Debt
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.domain.model.Wallet

data class DebtsState(
    val debts: List<Debt> = emptyList(),
    val wallets: List<Wallet> = emptyList(),
    val selectedType: DebtType? = null,
    val statusFilter: DebtStatusFilter = DebtStatusFilter.UNPAID,
    val isLoading: Boolean = false
) {
    val filteredDebts: List<Debt>
        get() = debts
            .filter { selectedType == null || it.type == selectedType }
            .filter {
                when (statusFilter) {
                    DebtStatusFilter.UNPAID -> it.remainingAmount > 0
                    DebtStatusFilter.PAID -> it.remainingAmount <= 0
                }
            }
}
