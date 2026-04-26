package dev.muffar.moneyfikasi.utils.extensions

import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDateTime
import org.threeten.bp.Month
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

object LocalDateTimeExt {
    fun LocalDateTime.startOfYear(): Long {
        return this.withMonth(1).withDayOfMonth(1).toMilliseconds()
    }

    fun LocalDateTime.endOfYear(): Long {
        return this.withMonth(12)
            .withDayOfMonth(31)
            .withHour(23)
            .withMinute(59)
            .withSecond(59)
            .withNano(999999999)
            .toMilliseconds()
    }

    fun LocalDateTime.startOfMonth(): Long {
        return this.withDayOfMonth(1).toMilliseconds()
    }

    fun LocalDateTime.endOfMonth(): Long {
        return this.withDayOfMonth(1)
            .plusMonths(1)
            .minusDays(1)
            .withHour(23)
            .withMinute(59)
            .withSecond(59)
            .withNano(999999999)
            .toMilliseconds()
    }

    fun LocalDateTime.startOfWeek(): Long {
        return this.with(DayOfWeek.MONDAY).toMilliseconds()
    }

    fun LocalDateTime.endOfWeek(): Long {
        return this.with(DayOfWeek.SUNDAY)
            .withHour(23)
            .withMinute(59)
            .withSecond(59)
            .withNano(999999999)
            .toMilliseconds()
    }

    fun LocalDateTime.endOfDay(): Long {
        return this.withHour(23).withMinute(59).withSecond(59).withNano(999999999).toMilliseconds()
    }

    fun LocalDateTime.startOfDay(): Long {
        return this.withHour(0).withMinute(0).withSecond(0).withNano(0).toMilliseconds()
    }

    fun LocalDateTime.format(pattern: String): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return this.format(formatter)
    }

    fun LocalDateTime.toMilliseconds(): Long {
        return atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    fun Month.shortName(): String {
        return name.substring(0, 3).capitalize()
    }

}