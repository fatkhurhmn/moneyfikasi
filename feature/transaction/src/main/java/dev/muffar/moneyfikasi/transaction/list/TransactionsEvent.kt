package dev.muffar.moneyfikasi.transaction.list

import dev.muffar.moneyfikasi.domain.utils.TransactionFilter

sealed class TransactionsEvent {
    data class OnExpandFabButton(val isExpanded: Boolean) : TransactionsEvent()
    data class OnFilterChanged(val filter: TransactionFilter) : TransactionsEvent()
    data class OnDateRangeChanged(val start: Long, val end: Long) : TransactionsEvent()
    data class OnShowFilterSheet(val show: Boolean) : TransactionsEvent()
}