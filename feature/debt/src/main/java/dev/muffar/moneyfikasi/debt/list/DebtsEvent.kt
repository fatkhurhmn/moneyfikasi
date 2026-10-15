package dev.muffar.moneyfikasi.debt.list

import java.util.UUID

sealed class DebtsEvent {
    data class StatusFilterChanged(val filter: DebtStatusFilter) : DebtsEvent()
    data class PaymentOpened(val debtId: UUID) : DebtsEvent()
    data object PaymentDismissed : DebtsEvent()
    data class PaymentAmountChanged(val amount: String) : DebtsEvent()
    data object PaymentConfirmed : DebtsEvent()
}
