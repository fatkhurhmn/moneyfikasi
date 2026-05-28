package dev.muffar.moneyfikasi.domain.utils

import dev.muffar.moneyfikasi.domain.model.TimePeriod
import org.threeten.bp.Instant
import org.threeten.bp.ZoneOffset

object RecurringScheduleCalculator {
    fun initialNextRun(
        startDate: Long,
        frequency: TimePeriod,
        skipFirstRun: Boolean,
    ): Long {
        return if (skipFirstRun) {
            nextRunAfter(startDate, frequency) ?: startDate
        } else {
            startDate
        }
    }

    fun nextRunOnOrAfter(
        startDate: Long,
        frequency: TimePeriod,
        targetDate: Long,
    ): Long {
        var nextRun = startDate
        while (nextRun < targetDate) {
            nextRun = nextRunAfter(nextRun, frequency) ?: return startDate
        }
        return nextRun
    }

    fun nextRunAfter(runDate: Long, frequency: TimePeriod): Long? {
        val runDateTime = Instant.ofEpochMilli(runDate)
            .atZone(ZoneOffset.UTC)
            .toLocalDateTime()

        val nextRunDateTime = when (frequency) {
            TimePeriod.DAILY -> runDateTime.plusDays(1)
            TimePeriod.WEEKLY -> runDateTime.plusWeeks(1)
            TimePeriod.MONTHLY -> runDateTime.plusMonths(1)
            TimePeriod.YEARLY -> runDateTime.plusYears(1)
            TimePeriod.ALL,
            TimePeriod.CUSTOM -> return null
        }

        return nextRunDateTime.atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
    }
}
