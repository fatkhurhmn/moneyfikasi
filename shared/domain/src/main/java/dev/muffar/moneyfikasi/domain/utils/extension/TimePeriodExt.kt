package dev.muffar.moneyfikasi.domain.utils.extension

import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.endOfDay
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.endOfMonth
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.endOfWeek
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.endOfYear
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.startOfDay
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.startOfMonth
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.startOfWeek
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.startOfYear
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime

fun TimePeriod.toDateRange(localDateTime: LocalDateTime? = null): DateRange {
    val dateTime = localDateTime?.with(LocalTime.MIN) ?: LocalDateTime.now().with(LocalTime.MIN)
    return when (this) {
        TimePeriod.DAILY -> DateRange(
            timePeriod = TimePeriod.DAILY,
            start = dateTime.startOfDay(),
            end = dateTime.endOfDay()
        )

        TimePeriod.WEEKLY -> DateRange(
            timePeriod = TimePeriod.WEEKLY,
            start = dateTime.startOfWeek(),
            end = dateTime.endOfWeek()
        )

        TimePeriod.MONTHLY -> DateRange(
            timePeriod = TimePeriod.MONTHLY,
            start = dateTime.startOfMonth(),
            end = dateTime.endOfMonth()
        )

        TimePeriod.YEARLY -> DateRange(
            timePeriod = TimePeriod.YEARLY,
            start = dateTime.startOfYear(),
            end = dateTime.endOfYear()
        )

        TimePeriod.ALL -> DateRange(
            timePeriod = TimePeriod.ALL,
            start = Long.MIN_VALUE,
            end = Long.MAX_VALUE
        )

        TimePeriod.CUSTOM -> DateRange(timePeriod = TimePeriod.CUSTOM)
    }
}