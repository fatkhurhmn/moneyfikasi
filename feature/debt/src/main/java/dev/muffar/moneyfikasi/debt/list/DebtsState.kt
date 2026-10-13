package dev.muffar.moneyfikasi.debt.list

import dev.muffar.moneyfikasi.domain.model.Debt
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.domain.model.Wallet
import java.util.UUID

data class DebtsState(
    val debts: List<Debt> = emptyList(),
    val wallets: List<Wallet> = emptyList(),
    val selectedType: DebtType? = null,
    val isLoading: Boolean = false,
    val showAddSheet: Boolean = false,
    val paymentDebtId: UUID? = null,
    val paymentAmount: String = "",
    val paymentAmountError: ErrorMessage = ErrorMessage(),
    val isRecordingPayment: Boolean = false
) {
    val filteredDebts: List<Debt>
        get() = if (selectedType == null) {
            debts
        } else {
            debts.filter { it.type == selectedType }
        }

    val paymentDebt: Debt?
        get() = debts.find { it.id == paymentDebtId }
}
