package dev.muffar.moneyfikasi.transaction.list

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.utils.TransactionDateFilter
import org.threeten.bp.LocalDateTime

sealed class TransactionsEvent {
    data class OnExpandFabButton(val isExpanded: Boolean) : TransactionsEvent()
    data class OnFilterChanged(val filter: TransactionDateFilter) : TransactionsEvent()
    data class OnDateRangeChanged(val start: Long, val end: Long) : TransactionsEvent()
    data class OnShowFilterSheet(val show: Boolean) : TransactionsEvent()
    data class OnFilterCategories(val categories: Set<Category>) : TransactionsEvent()
    data class OnFilterWallets(val wallets: Set<Wallet>) : TransactionsEvent()
    data class OnLocalDateTimeChange(val localDateTime: LocalDateTime) : TransactionsEvent()
    data object OnVisibilityClicked : TransactionsEvent()
    data object OnApplyFilter : TransactionsEvent()
}