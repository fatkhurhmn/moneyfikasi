package dev.muffar.moneyfikasi.utils.extensions

import android.net.Uri
import androidx.core.net.toUri
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.toNormalizedDouble
import java.text.DecimalFormatSymbols
import java.util.Locale


object StringExt {
    fun String.clearThousandFormat(): String {
        val symbols = DecimalFormatSymbols.getInstance()
        val groupingSeparator = symbols.groupingSeparator
        val decimalSeparator = symbols.decimalSeparator

        return this.replace(groupingSeparator.toString(), "")
            .replace(decimalSeparator.toString(), ".")
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

    fun String.normalizeAmountText(): String {
        return this
            .replace(
                Regex("""(?i)\b(\d+(?:[.,]\d+)?)\s*(rb|ribu|k|thousand)\b""")
            ) { match ->
                val number = match.groupValues[1].toNormalizedDouble()
                number.multiplyToPlainString(1_000)
            }

            .replace(
                Regex("""(?i)\b(\d+(?:[.,]\d+)?)\s*(jt|juta|m|million)\b""")
            ) { match ->
                val number = match.groupValues[1].toNormalizedDouble()
                number.multiplyToPlainString(1_000_000)
            }

            .replace(
                Regex("""(?i)\b(\d+(?:[.,]\d+)?)\s*(miliar|billion|b|bn)\b""")
            ) { match ->
                val number = match.groupValues[1].toNormalizedDouble()
                number.multiplyToPlainString(1_000_000_000)
            }
    }

    fun Double.multiplyToPlainString(multiplier: Int): String {
        val result = this * multiplier
        return if (result % 1.0 == 0.0) {
            result.toLong().toString()
        } else {
            result.toString()
        }
    }

    fun String.cleanJson(): String {
        val start = this.indexOf("{")
        val end = this.lastIndexOf("}")
        return if (start != -1 && end != -1 && end > start) {
            this.substring(start, end + 1)
        } else {
            this.replace("```json", "")
                .replace("```", "")
                .trim()
        }
    }
}