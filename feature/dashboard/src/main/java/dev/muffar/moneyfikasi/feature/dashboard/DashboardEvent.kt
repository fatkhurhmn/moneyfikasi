package dev.muffar.moneyfikasi.feature.dashboard

import dev.muffar.moneyfikasi.domain.model.DateRange

sealed class DashboardEvent {
    data object Refresh : DashboardEvent()
    data object ToggleBalanceVisibility : DashboardEvent()
    data class DateRangeChanged(val dateRange: DateRange) : DashboardEvent()
    data class ShowReportDateSheet(val show: Boolean) : DashboardEvent()

}