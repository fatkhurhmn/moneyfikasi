package dev.muffar.moneyfikasi.utils.extensions

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.formatThousand(): String {
    val decimalFormatter = DecimalFormat("#,###")
    return decimalFormatter.format(this)
}

fun Long.toFormattedDateTime(pattern: String): String {
    val date = Date(this)
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(date)
}