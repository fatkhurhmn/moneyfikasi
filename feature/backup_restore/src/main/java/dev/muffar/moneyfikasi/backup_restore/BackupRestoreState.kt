package dev.muffar.moneyfikasi.backup_restore

import dev.muffar.moneyfikasi.domain.model.TimePeriod

data class BackupRestoreState(
    val latestBackupName: String = "",
    val latestBackupDate: Long = 0L,
    val latestBackupFolder: String = "",
    val isAutoBackupEnabled: Boolean = false,
    val autoBackupUri: String = "",
    val autoBackupPeriod: TimePeriod = TimePeriod.DAILY,
    val isDeletePreviousBackup: Boolean = true,
    val isLoading: Boolean = false,
)
