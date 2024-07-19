package dev.muffar.moneyfikasi.backup_restore

sealed class BackupRestoreEvent {
    data object OnBackupData : BackupRestoreEvent()
    data object OnRestoreData : BackupRestoreEvent()
}