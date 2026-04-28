package dev.muffar.moneyfikasi.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AutoBackup(
    val isEnabled: Boolean = false,
    val uri: String = "",
    val period: String = TimePeriod.DAILY.name,
)