package dev.muffar.moneyfikasi.feature.dashboard

sealed class DashboardEvent {
    data object Refresh : DashboardEvent()
}