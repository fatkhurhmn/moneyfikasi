package dev.muffar.moneyfikasi.backup_restore

import android.net.Uri
import dev.muffar.moneyfikasi.domain.model.TimePeriod

sealed class BackupRestoreEvent {
    data class OnBackupData(val uri: Uri) : BackupRestoreEvent()
    data class OnRestoreData(val uri: Uri) : BackupRestoreEvent()
    data class OnAutoBackupEnabledChanged(val isEnabled: Boolean) : BackupRestoreEvent()
    data class OnAutoBackupUriChanged(val uri: Uri) : BackupRestoreEvent()
    data class OnAutoBackupPeriodChanged(val period: TimePeriod) : BackupRestoreEvent()
}
