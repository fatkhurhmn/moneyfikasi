package dev.muffar.moneyfikasi.debt.list

import dev.muffar.moneyfikasi.domain.model.DebtType

sealed class DebtsEvent {
    data class FilterChanged(val type: DebtType?) : DebtsEvent()
}
