package dev.muffar.moneyfikasi.debt.list

import dev.muffar.moneyfikasi.domain.model.DebtType
import java.util.UUID

sealed class DebtsEvent {
    data class FilterChanged(val type: DebtType?) : DebtsEvent()
    data class ShowAddSheetChanged(val show: Boolean) : DebtsEvent()
    data class PaymentOpened(val debtId: UUID) : DebtsEvent()
    data object PaymentDismissed : DebtsEvent()
    data class PaymentAmountChanged(val amount: String) : DebtsEvent()
    data object PaymentConfirmed : DebtsEvent()
}
