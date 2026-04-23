package dev.muffar.moneyfikasi.backup_restore

data class BackupRestoreState(
    val latestBackupName: String = "",
    val latestBackupDate: Long = 0L,
    val isLoading: Boolean = false,
)