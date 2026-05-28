package dev.muffar.moneyfikasi.feature.home

import dev.muffar.moneyfikasi.domain.model.DateRange

sealed class HomeEvent {
    data object Refresh : HomeEvent()
    data object ToggleBalanceVisibility : HomeEvent()
    data object ToggleReportVisibility : HomeEvent()
    data class DateRangeChanged(val dateRange: DateRange) : HomeEvent()
    data class ShowReportDateSheet(val show: Boolean) : HomeEvent()
    data class ShowCustomDateSheet(val show: Boolean) : HomeEvent()

}