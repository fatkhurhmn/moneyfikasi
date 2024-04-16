package dev.muffar.moneyfikasi.utils

import org.threeten.bp.LocalDateTime
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