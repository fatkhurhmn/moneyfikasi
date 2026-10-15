package dev.muffar.moneyfikasi.debt.menu

import dev.muffar.moneyfikasi.domain.model.Debt
import dev.muffar.moneyfikasi.domain.model.DebtType

data class DebtMenuState(
    val debts: List<Debt> = emptyList()
) {
    private fun unpaidOf(type: DebtType): List<Debt> {
        return debts.filter { it.type == type && it.remainingAmount > 0 }
    }

    val unpaidDebts: List<Debt>
        get() = unpaidOf(DebtType.DEBT)

    val unpaidReceivables: List<Debt>
        get() = unpaidOf(DebtType.RECEIVABLE)

    val unpaidDebtRemaining: Double
        get() = unpaidDebts.sumOf { it.remainingAmount }

    val unpaidReceivableRemaining: Double
        get() = unpaidReceivables.sumOf { it.remainingAmount }
}
