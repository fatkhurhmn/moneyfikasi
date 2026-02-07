package dev.muffar.moneyfikasi.statistic.main

import dev.muffar.moneyfikasi.domain.model.DateRange
import org.threeten.bp.LocalDateTime

sealed class StatisticEvent {
    data class DateRangeChanged(val dateRange: DateRange) : StatisticEvent()
    data class ShowChooseDateSheet(val show: Boolean) : StatisticEvent()
    data class ShowCustomDateSheet(val show: Boolean) : StatisticEvent()
    data class TimeReferenceChanged(val timeReference: LocalDateTime) : StatisticEvent()
}