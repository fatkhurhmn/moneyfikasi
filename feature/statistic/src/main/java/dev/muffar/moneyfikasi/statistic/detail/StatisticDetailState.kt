package dev.muffar.moneyfikasi.statistic.detail

import androidx.paging.PagingData
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class StatisticDetailState(
    val transactions: Flow<PagingData<Transaction>> = emptyFlow<PagingData<Transaction>>(),
    val totalAmount: Double = 0.0,
    val type: TransactionType = TransactionType.INCOME
)
