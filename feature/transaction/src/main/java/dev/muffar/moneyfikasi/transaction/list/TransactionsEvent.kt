package dev.muffar.moneyfikasi.transaction.list

import dev.muffar.moneyfikasi.domain.utils.TransactionFilter
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsSheetType

sealed class TransactionsEvent {
    data class OnExpandFabButton(val isExpanded: Boolean) : TransactionsEvent()
    data class OnFilterChanged(val filter: TransactionFilter) : TransactionsEvent()
    data class OnShowBottomSheet(val type: TransactionsSheetType?) : TransactionsEvent()
    data class OnDateRangeChanged(val start: Long, val end: Long) : TransactionsEvent()
}