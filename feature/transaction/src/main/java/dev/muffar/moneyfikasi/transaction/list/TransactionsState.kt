package dev.muffar.moneyfikasi.transaction.list

import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.utils.format

data class TransactionsState(
    val transactions: List<Transaction> = emptyList(),
    val transactionsByDate: Map<String, List<Transaction>> = transactions.groupBy {
        it.date.format("yyyy-MM-dd")
    },
    val isLoading : Boolean = false
)
