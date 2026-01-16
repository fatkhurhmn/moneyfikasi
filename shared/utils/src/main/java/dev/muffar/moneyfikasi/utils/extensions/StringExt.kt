package dev.muffar.moneyfikasi.utils.extensions

import java.util.Locale


fun String.clearThousandFormat(): String {
    return this.replace(",", "")
}

fun String.toEmptyUUID(): String {
    return "00000000-0000-0000-0000-000000000000"
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
fun String.capitalize(): String {
    return lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}