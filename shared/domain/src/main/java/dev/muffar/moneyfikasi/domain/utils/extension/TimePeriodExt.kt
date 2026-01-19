package dev.muffar.moneyfikasi.domain.utils.extension

import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.utils.extensions.endOfDay
import dev.muffar.moneyfikasi.utils.extensions.endOfMonth
import dev.muffar.moneyfikasi.utils.extensions.endOfWeek
import dev.muffar.moneyfikasi.utils.extensions.endOfYear
import dev.muffar.moneyfikasi.utils.extensions.startOfDay
import dev.muffar.moneyfikasi.utils.extensions.startOfMonth
import dev.muffar.moneyfikasi.utils.extensions.startOfWeek
import dev.muffar.moneyfikasi.utils.extensions.startOfYear
import org.threeten.bp.LocalDateTime

fun TimePeriod.toDateRange(localDateTime: LocalDateTime): DateRange {
    return when (this) {
        TimePeriod.DAILY -> DateRange(
            timePeriod = TimePeriod.DAILY,
            start = localDateTime.startOfDay(),
            end = localDateTime.endOfDay()
        )

        TimePeriod.WEEKLY -> DateRange(
            timePeriod = TimePeriod.WEEKLY,
            start = localDateTime.startOfWeek(),
            end = localDateTime.endOfWeek()
        )

        TimePeriod.MONTHLY -> DateRange(
            timePeriod = TimePeriod.MONTHLY,
            start = localDateTime.startOfMonth(),
            end = localDateTime.endOfMonth()
        )

        TimePeriod.YEARLY -> DateRange(
            timePeriod = TimePeriod.YEARLY,
            start = localDateTime.startOfYear(),
            end = localDateTime.endOfYear()
        )

        TimePeriod.ALL -> DateRange(
            timePeriod = TimePeriod.ALL,
            start = Long.MIN_VALUE,
            end = Long.MAX_VALUE
        )

        TimePeriod.CUSTOM -> DateRange(timePeriod = TimePeriod.CUSTOM)
    }
}