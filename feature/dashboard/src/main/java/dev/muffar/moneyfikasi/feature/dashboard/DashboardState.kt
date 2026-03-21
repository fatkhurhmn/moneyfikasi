package dev.muffar.moneyfikasi.feature.dashboard

data class DashboardState(
    val totalBalance: Double = 0.0,
    val isBalanceVisible: Boolean = true,
    val isLoading: Boolean = false,
)