package dev.muffar.moneyfikasi.domain.model

import kotlin.math.absoluteValue

data class TrendResult(
    val percentage: Double = 0.0,
    val type: TrendType = TrendType.NEUTRAL
) {
    val message: String
        get() = when (type) {
            TrendType.UP -> "${percentage.absoluteValue.toInt()}%"
            TrendType.DOWN -> "${percentage.absoluteValue.toInt()}%"
            TrendType.NEW_GROWTH -> "New growth"
            TrendType.NEW_LOSS -> "New loss"
            TrendType.NEUTRAL -> "No change"
        }
}