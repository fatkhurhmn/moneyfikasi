package dev.muffar.moneyfikasi.transaction.list

import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.model.TransactionFilter
import org.threeten.bp.LocalDateTime

sealed class TransactionsEvent {
    data class FloatingActionButtonClicked(val isExpanded: Boolean) : TransactionsEvent()
    data class DateRangeChanged(val dateRange: DateRange) : TransactionsEvent()
    data class ShowFilterSheet(val show: Boolean) : TransactionsEvent()
    data class ShowChooseDateSheet(val show: Boolean) : TransactionsEvent()
    data class ShowCustomDateSheet(val show: Boolean) : TransactionsEvent()
    data class DateTimeChanged(val dateTime: LocalDateTime) : TransactionsEvent()
    data object ResetFilter : TransactionsEvent()
    data class FilterChanged(val filter: TransactionFilter) : TransactionsEvent()
}