package dev.muffar.moneyfikasi.utils.extensions

import android.net.Uri
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

fun String.toDisplayPath(): String {
    if (this.isEmpty()) return ""
    val uri = Uri.parse(this)
    val path = uri.path?.let { Uri.decode(it) } ?: this
    // Handle SAF URIs like /tree/primary:Download or /tree/1234-ABCD:Music
    val displayPath = if (path.contains(":")) {
        path.split(":").lastOrNull() ?: path
    } else {
        path.split("/0/").lastOrNull() ?: path
    }
    return if (displayPath.startsWith("/")) displayPath else "/$displayPath"
}
