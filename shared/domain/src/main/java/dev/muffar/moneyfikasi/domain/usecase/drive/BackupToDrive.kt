package dev.muffar.moneyfikasi.domain.usecase.drive

import dev.muffar.moneyfikasi.domain.model.DriveBackupFile
import dev.muffar.moneyfikasi.domain.repository.DriveBackupRepository

class BackupToDrive(
    private val repository: DriveBackupRepository
) {
    suspend operator fun invoke(): Result<DriveBackupFile> {
        return repository.backupToDrive()
    }
}
