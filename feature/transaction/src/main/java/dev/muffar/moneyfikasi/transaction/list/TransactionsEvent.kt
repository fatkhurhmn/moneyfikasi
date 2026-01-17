package dev.muffar.moneyfikasi.transaction.list

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.utils.TransactionDateFilter
import org.threeten.bp.LocalDateTime

sealed class TransactionsEvent {
    data class FloatingActionButtonClicked(val isExpanded: Boolean) : TransactionsEvent()
    data class FilterChanged(val filter: TransactionDateFilter) : TransactionsEvent()
    data class DateRangeChanged(val start: Long, val end: Long) : TransactionsEvent()
    data class ShowFilterSheet(val show: Boolean) : TransactionsEvent()
    data class CategoryFiltered(val categories: Set<Category>) : TransactionsEvent()
    data class WalletFiltered(val wallets: Set<Wallet>) : TransactionsEvent()
    data class LocalDateTimeChanged(val localDateTime: LocalDateTime) : TransactionsEvent()
}