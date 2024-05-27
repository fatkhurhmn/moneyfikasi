package dev.muffar.moneyfikasi.search

import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.utils.format

data class SearchState(
    val searchQuery: String? = null,
    val transactions : List<Transaction> = emptyList(),
    val transactionsByDate: Map<String, List<Transaction>> = transactions.groupBy {
        it.date.format("yyyy-MM-dd")
    },
)
