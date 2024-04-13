package dev.muffar.moneyfikasi.utils

import org.threeten.bp.LocalDateTime
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