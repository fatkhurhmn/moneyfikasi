package dev.muffar.moneyfikasi.search

import androidx.paging.PagingData
import dev.muffar.moneyfikasi.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class SearchState(
    val searchQuery: String? = null,
    val transactions: Flow<PagingData<Transaction>> = emptyFlow<PagingData<Transaction>>(),
)
