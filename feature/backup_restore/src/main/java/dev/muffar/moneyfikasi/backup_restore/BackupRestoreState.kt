package dev.muffar.moneyfikasi.backup_restore

data class BackupRestoreState(
    val latestBackupName: String = "",
    val latestBackupDate: Long = 0L,
    val isAutoBackupEnabled: Boolean = false,
    val autoBackupUri: String = "",
    val autoBackupPeriod: String = "Daily",
    val isLoading: Boolean = false,
)