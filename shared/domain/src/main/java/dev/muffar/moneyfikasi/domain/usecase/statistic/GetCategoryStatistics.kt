package dev.muffar.moneyfikasi.domain.usecase.statistic

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryStatistic
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.repository.StatisticRepository
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetCategoryStatistics(
    private val repository: StatisticRepository,
) {
    operator fun invoke(
        dateRange: DateRange,
        categories: Set<Category>,
        wallets: Set<Wallet>,
        limit: Int? = null,
    ): Flow<Map<CategoryType, List<CategoryStatistic>>> {
        val categoriesIds = categories.map { it.id }.toSet()
        val walletIds = wallets.map { it.id }.toSet()

        return combine(
            repository.getCategoryStatistics(
                startDateRange = dateRange.start,
                endDateRange = dateRange.end,
                type = TransactionType.INCOME,
                categories = categoriesIds.ifEmpty { null },
                wallets = walletIds.ifEmpty { null },
                limit = limit
            ),
            repository.getCategoryStatistics(
                startDateRange = dateRange.start,
                endDateRange = dateRange.end,
                type = TransactionType.EXPENSE,
                categories = categoriesIds.ifEmpty { null },
                wallets = walletIds.ifEmpty { null },
                limit = limit
            )
        ) { incomeStats, expenseStats ->
            val totalIncome = incomeStats.sumOf { it.amount }
            val totalExpense = expenseStats.sumOf { it.amount }

            mapOf(
                CategoryType.INCOME to incomeStats.map {
                    it.copy(percentage = if (totalIncome > 0) it.amount / totalIncome else 0.0)
                },
                CategoryType.EXPENSE to expenseStats.map {
                    it.copy(percentage = if (totalExpense > 0) it.amount / totalExpense else 0.0)
                }
            )
        }
    }
}
