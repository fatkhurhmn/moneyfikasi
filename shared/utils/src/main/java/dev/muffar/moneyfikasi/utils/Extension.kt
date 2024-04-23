package dev.muffar.moneyfikasi.utils

import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDateTime
import org.threeten.bp.Month
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.formatThousand(): String {
    val decimalFormatter = DecimalFormat("#,###")
    return decimalFormatter.format(this)
}

fun String.clearThousandFormat(): String {
    return this.replace(",", "")
}

fun Long.toFormattedDateTime(pattern: String): String {
    val date = Date(this)
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(date)
}

fun String.toEmptyUUID(): String {
    return "00000000-0000-0000-0000-000000000000"
}

fun LocalDateTime.format(pattern: String): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return this.format(formatter)
}

fun String.filterAmount(): String? {
    return if (length < 20) {
        val filtered = filter { it.isDigit() }
        val parsedValue = if (filtered.isNotBlank()) {
            filtered.clearThousandFormat().toLong().formatThousand()
        } else {
            "0"
        }
        parsedValue
    } else {
        null
    }
}

fun LocalDateTime.toMilliseconds(): Long {
    return atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun String.capitalize(): String {
    return lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

fun Month.shortName(): String {
    return name.substring(0, 3).capitalize()
}

fun LocalDateTime.startOfYear(): Long {
    return this.withMonth(1).withDayOfMonth(1).toMilliseconds()
}

fun LocalDateTime.endOfYear(): Long {
    return this.withMonth(12).withDayOfMonth(31).toMilliseconds()
}

fun LocalDateTime.startOfMonth(): Long {
    return this.withDayOfMonth(1).toMilliseconds()
}

fun LocalDateTime.endOfMonth(): Long {
    return this.withDayOfMonth(1).plusMonths(1).minusDays(1).toMilliseconds()
}

fun LocalDateTime.startOfWeek(): Long {
    return this.with(DayOfWeek.MONDAY).toMilliseconds()
}

fun LocalDateTime.endOfWeek(): Long {
    return this.with(DayOfWeek.SUNDAY).toMilliseconds()
}

fun LocalDateTime.endOfDay(): Long {
    return this.withHour(23).withMinute(59).withSecond(59).withNano(999999999).toMilliseconds()
}

fun LocalDateTime.startOfDay(): Long {
    return this.withHour(0).withMinute(0).withSecond(0).withNano(0).toMilliseconds()
}

fun Double.format(digits: Int) = "%.${digits}f".format(this)