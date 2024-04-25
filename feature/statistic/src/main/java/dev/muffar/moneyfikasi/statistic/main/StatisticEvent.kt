package dev.muffar.moneyfikasi.statistic.main

import dev.muffar.moneyfikasi.domain.utils.TransactionDateFilter
import dev.muffar.moneyfikasi.statistic.main.component.StatisticSheetType

sealed class StatisticEvent {
    data class OnDateRangeChanged(val start: Long, val end: Long) : StatisticEvent()
    data class OnShowBottomSheet(val type: StatisticSheetType?) : StatisticEvent()
    data class OnFilterChanged(val filter: TransactionDateFilter) : StatisticEvent()
}