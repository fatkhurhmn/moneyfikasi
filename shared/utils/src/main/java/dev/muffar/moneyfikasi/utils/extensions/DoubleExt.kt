package dev.muffar.moneyfikasi.utils.extensions

fun Double.format(digits: Int) = "%.${digits}f".format(this)
