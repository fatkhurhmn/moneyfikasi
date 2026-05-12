package dev.muffar.moneyfikasi.domain.usecase.statistic

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.model.TransactionTrend
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.repository.StatisticRepository
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.format
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import java.util.Locale

class GetTransactionTrend(
    private val repository: StatisticRepository,
) {
    operator fun invoke(
        dateRange: DateRange,
        categories: Set<Category>,
        wallets: Set<Wallet>,
    ): Flow<TransactionTrend> {
        val categoriesIds = categories.map { it.id }.toSet()
        val walletIds = wallets.map { it.id }.toSet()
        return repository.getTransactionTrendItems(
            startDateRange = dateRange.start,
            endDateRange = dateRange.end,
            categories = categoriesIds.ifEmpty { null },
            wallets = walletIds.ifEmpty { null }
        ).map { transactions ->
            val incomeTransactions = transactions.filter { it.isIncome }
            val expenseTransactions = transactions.filter { it.isExpense }

            val labels = mutableListOf<String>()
            val incomeValues = mutableListOf<Double>()
            val expenseValues = mutableListOf<Double>()
            val start = LocalDateTime.ofInstant(Instant.ofEpochMilli(dateRange.start), ZoneId.systemDefault())

            when (dateRange.timePeriod) {
                TimePeriod.DAILY -> {
                    val income = incomeTransactions.groupBy { it.date.hour }
                    val expense = expenseTransactions.groupBy { it.date.hour }
                    for (i in 0 until 24) {
                        labels.add(String.format(Locale.getDefault(), "%02d:00", i))
                        incomeValues.add(income[i]?.sumOf { it.amount } ?: 0.0)
                        expenseValues.add(expense[i]?.sumOf { it.amount } ?: 0.0)
                    }
                }

                TimePeriod.WEEKLY -> {
                    val income = incomeTransactions.groupBy { it.date.toLocalDate() }
                    val expense = expenseTransactions.groupBy { it.date.toLocalDate() }
                    for (i in 0 until 7) {
                        val day = start.plusDays(i.toLong())
                        labels.add(day.format("dd MMM"))
                        incomeValues.add(income[day.toLocalDate()]?.sumOf { it.amount } ?: 0.0)
                        expenseValues.add(expense[day.toLocalDate()]?.sumOf { it.amount } ?: 0.0)
                    }
                }

                TimePeriod.MONTHLY -> {
                    val days = start.plusMonths(1).minusDays(1).dayOfMonth
                    val income = incomeTransactions.groupBy { it.date.dayOfMonth }
                    val expense = expenseTransactions.groupBy { it.date.dayOfMonth }
                    for (i in 1..days) {
                        labels.add(start.withDayOfMonth(i).format("dd MMM"))
                        incomeValues.add(income[i]?.sumOf { it.amount } ?: 0.0)
                        expenseValues.add(expense[i]?.sumOf { it.amount } ?: 0.0)
                    }
                }

                TimePeriod.YEARLY -> {
                    val income = incomeTransactions.groupBy { it.date.monthValue }
                    val expense = expenseTransactions.groupBy { it.date.monthValue }
                    for (i in 0 until 12) {
                        val month = start.plusMonths(i.toLong())
                        labels.add(month.format("MMM yy"))
                        incomeValues.add(income[month.monthValue]?.sumOf { it.amount } ?: 0.0)
                        expenseValues.add(expense[month.monthValue]?.sumOf { it.amount } ?: 0.0)
                    }
                }

                else -> {
                    labels.add("Total")
                    incomeValues.add(incomeTransactions.sumOf { it.amount })
                    expenseValues.add(expenseTransactions.sumOf { it.amount })
                }
            }
            TransactionTrend(labels, incomeValues, expenseValues)
        }
    }
}
