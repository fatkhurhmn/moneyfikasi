package dev.muffar.moneyfikasi.domain.repository

import dev.muffar.moneyfikasi.domain.model.DriveBackupFile
import java.io.File

interface DriveBackupRepository {
    suspend fun isSignedIn(): Boolean
    suspend fun signOut()
    suspend fun getDriveBackups(): Result<List<DriveBackupFile>>
    suspend fun backupToDrive(): Result<DriveBackupFile>
    suspend fun restoreFromDrive(fileId: String, restart: Boolean = true): Result<Unit>
    suspend fun deleteDriveBackup(fileId: String): Result<Unit>
    suspend fun uploadFile(file: File, fileName: String): Result<DriveBackupFile>
}
