package dev.muffar.moneyfikasi.transaction.list

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.utils.TransactionFilter
import dev.muffar.moneyfikasi.utils.format
import dev.muffar.moneyfikasi.utils.startOfMonth
import org.threeten.bp.LocalDateTime

data class TransactionsState(
    val transactions: List<Transaction> = emptyList(),
    val transactionsByDate: Map<String, List<Transaction>> = transactions.groupBy {
        it.date.format("yyyy-MM-dd")
    },
    val isLoading: Boolean = false,
    val isExpandedFab : Boolean = false,
    val filter: TransactionFilter = TransactionFilter.MONTHLY,
    val categories : List<Category> = emptyList(),
    val wallets : List<Wallet> = emptyList(),
    val selectedCategories : Set<Category> = emptySet(),
    val selectedWallets : Set<Wallet> = emptySet(),
    val startDateRange: Long = LocalDateTime.now().startOfMonth(),
    val endDateRange: Long = LocalDateTime.now().startOfMonth(),
    val showFilterSheet : Boolean = false,
    val showDateRangeSheet : Boolean = false,
    val overviewIncome : Double = 0.0,
    val overviewExpense : Double = 0.0,
    val overviewTotal : Double = 0.0,
    val totalBalance : Double = 0.0
)
