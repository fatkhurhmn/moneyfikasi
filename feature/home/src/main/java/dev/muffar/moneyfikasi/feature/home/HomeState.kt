package dev.muffar.moneyfikasi.feature.home

import dev.muffar.moneyfikasi.domain.model.Budget
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.model.Preset
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.TrendResult
import dev.muffar.moneyfikasi.domain.model.Wallet

data class HomeState(
    val totalBalance: Double = 0.0,
    val reportNet: Double = 0.0,
    val reportIncome: Double = 0.0,
    val reportExpense: Double = 0.0,
    val balanceTrend: TrendResult = TrendResult(),
    val savingPercentage: Double = 0.0,
    val isBalanceVisible: Boolean = true,
    val isReportVisible: Boolean = true,
    val isLoading: Boolean = false,
    val dateRange: DateRange = DateRange(),
    val categories: Set<Category> = emptySet(),
    val wallets: Set<Wallet> = emptySet(),
    val recentTransactions: List<Transaction> = emptyList(),
    val presets: List<Preset> = emptyList(),
    val budgets: List<Budget> = emptyList(),
    val showReportDateSheet: Boolean = false,
    val showCustomDateSheet: Boolean = false,
)
