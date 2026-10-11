package dev.muffar.moneyfikasi.domain.repository

import android.net.Uri
import dev.muffar.moneyfikasi.domain.model.LatestBackup
import dev.muffar.moneyfikasi.domain.model.TimePeriod

interface BackupRestoreRepository {
    suspend fun backupData(uri: Uri): Result<String>
    suspend fun restoreData(uri: Uri, restart: Boolean = true): Result<Unit>
    suspend fun deleteBackup(latestBackup: LatestBackup): Result<Unit>
    fun takePersistableUriPermission(uri: Uri)
    fun scheduleAutoBackup(isEnabled: Boolean, uri: String, period: TimePeriod)
    fun cancelAutoBackup()
    fun scheduleDriveAutoBackup(isSignedIn: Boolean, period: TimePeriod)
    fun cancelDriveAutoBackup()
}
