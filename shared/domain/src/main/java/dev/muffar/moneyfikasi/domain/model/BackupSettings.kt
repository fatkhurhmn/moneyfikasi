package dev.muffar.moneyfikasi.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class BackupSettings(
    val latestBackup: LatestBackup = LatestBackup(),
    val autoBackup: AutoBackup = AutoBackup(),
    val isDeletePreviousBackup: Boolean = true
)