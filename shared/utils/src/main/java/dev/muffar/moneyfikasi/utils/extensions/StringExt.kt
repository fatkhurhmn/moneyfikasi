package dev.muffar.moneyfikasi.utils.extensions

import java.util.Locale


fun String.clearThousandFormat(): String {
    return this.replace(",", "")
}

fun String.filterAmount(): String? {
    return if (length < 20) {
        val filtered = filter { it.isDigit() }
        val parsedValue = if (filtered.isNotBlank()) {
            filtered.clearThousandFormat().toDouble().formatThousand()
        } else {
            ""
        }
        parsedValue
    } else {
        null
    }
}
fun String.capitalize(): String {
    return lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}