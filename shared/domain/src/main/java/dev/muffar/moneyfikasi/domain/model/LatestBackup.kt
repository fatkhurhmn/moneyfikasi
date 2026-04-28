package dev.muffar.moneyfikasi.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LatestBackup(
    val name: String = "",
    val date: Long = 0L,
    val folder: String = "",
)