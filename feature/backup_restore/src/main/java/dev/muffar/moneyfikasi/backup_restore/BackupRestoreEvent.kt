package dev.muffar.moneyfikasi.backup_restore

sealed class BackupRestoreEvent {
    data object OnBackupData : BackupRestoreEvent()
    data object OnRestoreData : BackupRestoreEvent()
    data class OnShowBackupAlert(val showAlertDialog: Boolean) : BackupRestoreEvent()
    data class OnShowRestoreAlert(val showAlertDialog: Boolean) : BackupRestoreEvent()
}