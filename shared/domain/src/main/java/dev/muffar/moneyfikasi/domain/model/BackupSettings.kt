package dev.muffar.moneyfikasi.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class BackupSettings(
    val latestBackupName: String = "",
    val latestBackupDate: Long = 0L,
    val latestBackupFolder: String = "",
    val isAutoBackupEnabled: Boolean = false,
    val autoBackupUri: String = "",
    val autoBackupPeriod: String = TimePeriod.DAILY.name,
    val isDeletePreviousBackup: Boolean = true
)