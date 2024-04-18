package dev.muffar.moneyfikasi.transaction.list

import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.utils.TransactionFilter
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsSheetType
import dev.muffar.moneyfikasi.utils.format
import dev.muffar.moneyfikasi.utils.startOfMonth
import org.threeten.bp.LocalDateTime

data class TransactionsState(
    val transactions: List<Transaction> = emptyList(),
    val transactionsByDate: Map<String, List<Transaction>> = transactions.groupBy {
        it.date.format("yyyy-MM-dd")
    },
    val isLoading: Boolean = false,
    val filter: TransactionFilter = TransactionFilter.MONTHLY,
    val sheetType: TransactionsSheetType? = null,
    val showCustomDateDialog: Boolean = false,
    val startDateRange: Long = LocalDateTime.now().startOfMonth(),
    val endDateRange: Long = LocalDateTime.now().startOfMonth(),
)
