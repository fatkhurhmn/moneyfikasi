package dev.muffar.moneyfikasi.debt.list

sealed class DebtsEvent {
    data class StatusFilterChanged(val filter: DebtStatusFilter) : DebtsEvent()
}
