package dev.muffar.moneyfikasi.statistic.main

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.utils.TransactionFilter
import dev.muffar.moneyfikasi.statistic.main.component.StatisticSheetType
import dev.muffar.moneyfikasi.utils.startOfMonth
import org.threeten.bp.LocalDateTime

data class StatisticState(
    val categories : Set<Category> = emptySet(),
    val wallets : Set<Wallet> = emptySet(),
    val filter : TransactionFilter = TransactionFilter.MONTHLY,
    val startDateRange  : Long = LocalDateTime.now().startOfMonth(),
    val endDateRange  : Long = LocalDateTime.now().startOfMonth(),
    val overviewIncome: Double = 0.0,
    val overviewExpense: Double = 0.0,
    val overviewTotal: Double = 0.0,
    val sheetType : StatisticSheetType? = null
)
