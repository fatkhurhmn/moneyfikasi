package dev.muffar.moneyfikasi.domain.usecase.drive

import dev.muffar.moneyfikasi.domain.repository.DriveBackupRepository

class IsDriveSignedIn(
    private val repository: DriveBackupRepository
) {

    suspend operator fun invoke(): Boolean {
        return try {
            repository.isSignedIn()
        } catch (_: Exception) {
            false
        }
    }
}
