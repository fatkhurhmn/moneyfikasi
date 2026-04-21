package dev.muffar.moneyfikasi.transaction.list

import androidx.paging.PagingData
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.TransactionFilter
import dev.muffar.moneyfikasi.domain.model.Wallet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime

data class TransactionsState(
    val transactions: Flow<PagingData<Transaction>> = emptyFlow(),
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val wallets: List<Wallet> = emptyList(),
    val timeReference: LocalDateTime = LocalDateTime.now().with(LocalTime.MIN),
    val filter: TransactionFilter = TransactionFilter(),
    val dateRange: DateRange = DateRange(),
    val isFilterApplied: Boolean = false,
    val showFilterSheet: Boolean = false,
    val showChooseDateSheet: Boolean = false,
    val showCustomDateSheet: Boolean = false,
)
