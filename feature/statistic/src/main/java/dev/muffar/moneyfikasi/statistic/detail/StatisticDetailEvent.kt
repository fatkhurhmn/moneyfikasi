package dev.muffar.moneyfikasi.statistic.detail

import java.util.UUID

sealed class StatisticDetailEvent {
    data class InitState(
        val dateRange: Pair<Long, Long>,
        val categoryId: UUID
    ) : StatisticDetailEvent()
}
