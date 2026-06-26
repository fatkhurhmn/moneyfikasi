package dev.muffar.moneyfikasi.utils.extensions

import java.text.DecimalFormat

object DoubleExt {
    fun Double.formatThousand(): String {
        val decimalFormatter = DecimalFormat("#,###")
        return decimalFormatter.format(this)
    }

    fun String.toNormalizedDouble(): Double {
        return this
            .replace(",", ".")
            .toDoubleOrNull() ?: 0.0
    }
}