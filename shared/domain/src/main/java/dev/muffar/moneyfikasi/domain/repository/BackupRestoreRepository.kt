package dev.muffar.moneyfikasi.domain.repository

import android.net.Uri

interface BackupRestoreRepository {
    suspend fun backupData(uri: Uri): Result<String>
    suspend fun restoreData(uri: Uri, restart: Boolean = true): Result<Unit>
    suspend fun deleteBackup(uri: Uri, fileName: String): Result<Unit>
}
