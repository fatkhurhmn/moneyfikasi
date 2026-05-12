package dev.muffar.moneyfikasi.domain.repository

import dev.muffar.moneyfikasi.domain.model.CategoryStatistic
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.TransactionTrendItem
import dev.muffar.moneyfikasi.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface StatisticRepository {
    fun getCategoryStatistics(
        startDateRange: Long,
        endDateRange: Long,
        type: TransactionType,
        categories: Set<UUID>?,
        wallets: Set<UUID>?,
        limit: Int?,
    ): Flow<List<CategoryStatistic>>

    fun getTransactionTrendItems(
        startDateRange: Long,
        endDateRange: Long,
        categories: Set<UUID>?,
        wallets: Set<UUID>?
    ): Flow<List<TransactionTrendItem>>

    fun getHighestTransaction(
        startDateRange: Long,
        endDateRange: Long,
        type: TransactionType,
        categories: Set<UUID>?,
        wallets: Set<UUID>?
    ): Flow<Transaction?>

    fun getMostFrequentCategory(
        startDateRange: Long,
        endDateRange: Long,
        type: TransactionType,
        categories: Set<UUID>?,
        wallets: Set<UUID>?
    ): Flow<CategoryStatistic?>
}
