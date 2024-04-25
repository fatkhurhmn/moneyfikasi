package dev.muffar.moneyfikasi.statistic.main

import dev.muffar.moneyfikasi.domain.utils.TransactionDateFilter
import dev.muffar.moneyfikasi.statistic.main.component.StatisticSheetType
import org.threeten.bp.LocalDateTime

sealed class StatisticEvent {
    data class OnDateRangeChanged(val start: Long, val end: Long) : StatisticEvent()
    data class OnShowBottomSheet(val type: StatisticSheetType?) : StatisticEvent()
    data class OnFilterChanged(val filter: TransactionDateFilter) : StatisticEvent()
    data class OnLocalDateTimeChange(val localDateTime: LocalDateTime) : StatisticEvent()
}