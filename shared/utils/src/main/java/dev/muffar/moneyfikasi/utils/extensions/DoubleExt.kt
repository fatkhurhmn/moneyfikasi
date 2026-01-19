package dev.muffar.moneyfikasi.utils.extensions

import java.text.DecimalFormat

fun Double.formatThousand(): String {
    val decimalFormatter = DecimalFormat("#,###")
    return decimalFormatter.format(this)
}