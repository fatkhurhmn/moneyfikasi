package dev.muffar.moneyfikasi.domain.usecase.statistic

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.model.StatisticInsight
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.repository.StatisticRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetStatisticInsights(
    private val repository: StatisticRepository,
) {
    operator fun invoke(
        dateRange: DateRange,
        categories: Set<Category>,
        wallets: Set<Wallet>,
    ): Flow<StatisticInsight> {
        val categoriesIds = categories.map { it.id }.toSet()
        val walletIds = wallets.map { it.id }.toSet()

        return combine(
            repository.getHighestTransaction(
                startDateRange = dateRange.start,
                endDateRange = dateRange.end,
                type = TransactionType.INCOME,
                categories = categoriesIds.ifEmpty { null },
                wallets = walletIds.ifEmpty { null }
            ),
            repository.getHighestTransaction(
                startDateRange = dateRange.start,
                endDateRange = dateRange.end,
                type = TransactionType.EXPENSE,
                categories = categoriesIds.ifEmpty { null },
                wallets = walletIds.ifEmpty { null }
            ),
            repository.getMostFrequentCategory(
                startDateRange = dateRange.start,
                endDateRange = dateRange.end,
                type = TransactionType.INCOME,
                categories = categoriesIds.ifEmpty { null },
                wallets = walletIds.ifEmpty { null }
            ),
            repository.getMostFrequentCategory(
                startDateRange = dateRange.start,
                endDateRange = dateRange.end,
                type = TransactionType.EXPENSE,
                categories = categoriesIds.ifEmpty { null },
                wallets = walletIds.ifEmpty { null }
            )
        ) { highestIncome, highestExpense, topIncomeCat, topExpenseCat ->
            StatisticInsight(
                highestIncome = highestIncome,
                highestExpense = highestExpense,
                mostFrequentIncomeCategory = topIncomeCat,
                mostFrequentExpenseCategory = topExpenseCat
            )
        }
    }
}
