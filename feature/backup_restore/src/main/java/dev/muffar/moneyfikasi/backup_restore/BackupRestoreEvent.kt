package dev.muffar.moneyfikasi.backup_restore

import android.net.Uri

sealed class BackupRestoreEvent {
    data class OnBackupData(val uri: Uri) : BackupRestoreEvent()
    data class OnRestoreData(val uri: Uri) : BackupRestoreEvent()
}