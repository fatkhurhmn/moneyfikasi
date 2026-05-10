package dev.muffar.moneyfikasi.data.repositoy

import dev.muffar.moneyfikasi.data.db.dao.TransactionDao
import dev.muffar.moneyfikasi.data.mapper.toDomain
import dev.muffar.moneyfikasi.domain.model.CategoryStatistic
import dev.muffar.moneyfikasi.domain.model.TransactionTrendItem
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.repository.StatisticRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class StatisticRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
) : StatisticRepository {
    override fun getCategoryStatistics(
        startDateRange: Long,
        endDateRange: Long,
        type: TransactionType,
        categories: Set<UUID>?,
        wallets: Set<UUID>?,
        limit: Int?,
    ): Flow<List<CategoryStatistic>> {
        return transactionDao.getCategoryStatistics(
            start = startDateRange,
            end = endDateRange,
            type = type,
            categories = categories,
            wallets = wallets,
            limit = limit
        ).map { list -> list.map { it.toDomain() } }
    }

    override fun getTransactionTrendItems(
        startDateRange: Long,
        endDateRange: Long,
        categories: Set<UUID>?,
        wallets: Set<UUID>?
    ): Flow<List<TransactionTrendItem>> {
        return transactionDao.getTransactionTrendItems(
            start = startDateRange,
            end = endDateRange,
            categories = categories,
            wallets = wallets
        ).map { list -> list.map { it.toDomain() } }
    }
}