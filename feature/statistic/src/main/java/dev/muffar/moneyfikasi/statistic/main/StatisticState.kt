package dev.muffar.moneyfikasi.statistic.main

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.utils.TransactionDateFilter
import dev.muffar.moneyfikasi.statistic.main.component.StatisticSheetType
import dev.muffar.moneyfikasi.utils.startOfMonth
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime

data class StatisticState(
    val incomeTransactions : List<Transaction> = emptyList(),
    val expenseTransactions : List<Transaction> = emptyList(),
    val categories : Set<Category> = emptySet(),
    val wallets : Set<Wallet> = emptySet(),
    val filter : TransactionDateFilter = TransactionDateFilter.MONTHLY,
    val currentLocalDateTime : LocalDateTime = LocalDateTime.now().with(LocalTime.MIN),
    val startDateRange  : Long = LocalDateTime.now().startOfMonth(),
    val endDateRange  : Long = LocalDateTime.now().startOfMonth(),
    val overviewIncome: Double = 0.0,
    val overviewExpense: Double = 0.0,
    val overviewTotal: Double = 0.0,
    val sheetType : StatisticSheetType? = null,
    val tabs : List<String> = CategoryType.entries.map { it.name }.reversed(),
)
