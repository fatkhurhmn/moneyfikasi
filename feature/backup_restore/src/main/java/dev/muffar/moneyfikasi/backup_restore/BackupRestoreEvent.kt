package dev.muffar.moneyfikasi.backup_restore

import android.net.Uri
import dev.muffar.moneyfikasi.domain.model.TimePeriod

sealed class BackupRestoreEvent {
    data class BackupData(val uri: Uri) : BackupRestoreEvent()
    data class RestoreData(val uri: Uri) : BackupRestoreEvent()
    data class AutoBackupEnabledChanged(val isEnabled: Boolean) : BackupRestoreEvent()
    data class AutoBackupUriChanged(val uri: Uri) : BackupRestoreEvent()
    data class AutoBackupPeriodChanged(val period: TimePeriod) : BackupRestoreEvent()
    data class DeletePreviousBackupChanged(val isEnabled: Boolean) : BackupRestoreEvent()
    data object DriveSignInResult : BackupRestoreEvent()
    data class DriveSignInHandled(val success: Boolean) : BackupRestoreEvent()
    data object DriveSignOut : BackupRestoreEvent()
    data object DriveLoadBackups : BackupRestoreEvent()
    data object DriveBackupNow : BackupRestoreEvent()
    data object DriveRestore : BackupRestoreEvent()
}
