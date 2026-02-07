package dev.muffar.moneyfikasi.statistic.main

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.Wallet
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime

data class StatisticState(
    val incomeTransactions : List<Transaction> = emptyList(),
    val expenseTransactions : List<Transaction> = emptyList(),
    val categories : Set<Category> = emptySet(),
    val wallets : Set<Wallet> = emptySet(),
    val timeReference : LocalDateTime = LocalDateTime.now().with(LocalTime.MIN),
    val dateRange : DateRange = DateRange(),
    val overviewIncome: Double = 0.0,
    val overviewExpense: Double = 0.0,
    val overviewTotal: Double = 0.0,
    val tabs : List<String> = CategoryType.entries.map { it.name }.reversed(),
    val showChooseDateSheet: Boolean = false,
    val showCustomDateSheet: Boolean = false,
)
