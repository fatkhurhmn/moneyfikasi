package dev.muffar.moneyfikasi.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class DriveBackupFile(
    val id: String,
    val name: String,
    val modifiedTime: Long = 0L,
    val size: Long = 0L
)
