package dev.muffar.moneyfikasi.utils.extensions

import java.text.DecimalFormat

object DoubleExt {
    fun Double.formatThousand(): String {
        val decimalFormatter = DecimalFormat("#,###")
        return decimalFormatter.format(this)
    }

    fun Double.formatCompact(): String {
        val abs = kotlin.math.abs(this)
        val sign = if (this < 0) "-" else ""
        return when {
            abs < 1000 -> DecimalFormat("#,###").format(this)
            abs < 1_000_000 -> {
                val v = abs / 1000.0
                val formatted = if (v % 1 == 0.0) {
                    DecimalFormat("#,###").format(v)
                } else {
                    val df = DecimalFormat("#,###.#")
                    df.format(v)
                }
                sign + formatted + "k"
            }
            abs < 1_000_000_000 -> {
                val v = abs / 1_000_000.0
                val formatted = if (v % 1 == 0.0) DecimalFormat("#,###").format(v) else DecimalFormat("#,###.#").format(v)
                sign + formatted + "M"
            }
            else -> {
                val v = abs / 1_000_000_000.0
                val formatted = if (v % 1 == 0.0) DecimalFormat("#,###").format(v) else DecimalFormat("#,###.#").format(v)
                sign + formatted + "B"
            }
        }
    }

    fun String.toNormalizedDouble(): Double {
        return this
            .replace(",", ".")
            .toDoubleOrNull() ?: 0.0
    }
}