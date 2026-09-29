package dev.muffar.moneyfikasi.domain.usecase.drive

import dev.muffar.moneyfikasi.domain.repository.DriveBackupRepository

class RestoreFromDrive(
    private val repository: DriveBackupRepository
) {
    suspend operator fun invoke(fileId: String): Result<Unit> {
        return repository.restoreFromDrive(fileId)
    }
}
