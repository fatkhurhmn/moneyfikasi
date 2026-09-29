package dev.muffar.moneyfikasi.domain.usecase.drive

import dev.muffar.moneyfikasi.domain.model.DriveBackupFile
import dev.muffar.moneyfikasi.domain.repository.DriveBackupRepository

class GetDriveBackups(
    private val repository: DriveBackupRepository
) {
    suspend operator fun invoke(): Result<List<DriveBackupFile>> {
        return repository.getDriveBackups()
    }
}
