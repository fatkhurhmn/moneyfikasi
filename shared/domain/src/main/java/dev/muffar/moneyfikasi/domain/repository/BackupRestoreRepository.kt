package dev.muffar.moneyfikasi.domain.repository

import android.net.Uri
import dev.muffar.moneyfikasi.domain.model.LatestBackup

interface BackupRestoreRepository {
    suspend fun backupData(uri: Uri): Result<String>
    suspend fun restoreData(uri: Uri, restart: Boolean = true): Result<Unit>
    suspend fun deleteBackup(latestBackup: LatestBackup): Result<Unit>
}
