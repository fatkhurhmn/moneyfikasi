package dev.muffar.moneyfikasi.feature.dashboard

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.model.Wallet

data class DashboardState(
    val totalBalance: Double = 0.0,
    val reportBalance: Double = 0.0,
    val reportIncome: Double = 0.0,
    val reportExpense: Double = 0.0,
    val isBalanceVisible: Boolean = true,
    val isLoading: Boolean = false,
    val dateRange : DateRange = DateRange(),
    val categories: Set<Category> = emptySet(),
    val wallets: Set<Wallet> = emptySet(),
    val showReportDateSheet: Boolean = false,
)