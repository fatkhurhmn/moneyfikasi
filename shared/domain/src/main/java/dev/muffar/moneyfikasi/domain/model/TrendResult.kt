package dev.muffar.moneyfikasi.domain.model

data class TrendResult(
    val percentage: Double = 0.0,
    val type: TrendType = TrendType.NEUTRAL
)
