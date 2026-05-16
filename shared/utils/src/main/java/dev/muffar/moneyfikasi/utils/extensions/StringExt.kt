package dev.muffar.moneyfikasi.utils.extensions

import android.net.Uri
import androidx.core.net.toUri
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand
import java.util.Locale


object StringExt {
    fun String.clearThousandFormat(): String {
        return this.replace(",", "")
    }

    fun String.filterAmount(maxDigits: Int = 15): String? {
        val filtered = filter { it.isDigit() }

        return if (filtered.length <= maxDigits) {
            if (filtered.isNotBlank()) {
                filtered
                    .clearThousandFormat()
                    .toDouble()
                    .formatThousand()
            } else {
                "0"
            }
        } else {
            null
        }
    }

    fun String.capitalize(): String {
        return lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }

    fun String.toDisplayPath(): String {
        if (this.isEmpty()) return ""
        val uri = this.toUri()
        val path = uri.path?.let { Uri.decode(it) } ?: this
        val displayPath = if (path.contains(":")) {
            path.split(":").lastOrNull() ?: path
        } else {
            path.split("/0/").lastOrNull() ?: path
        }
        return if (displayPath.startsWith("/")) displayPath else "/$displayPath"
    }
}