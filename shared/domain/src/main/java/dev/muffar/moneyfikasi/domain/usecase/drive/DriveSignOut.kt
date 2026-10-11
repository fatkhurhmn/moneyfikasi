package dev.muffar.moneyfikasi.domain.usecase.drive

import dev.muffar.moneyfikasi.domain.repository.DriveBackupRepository

class DriveSignOut(
    private val repository: DriveBackupRepository
) {

    suspend operator fun invoke() {
        repository.signOut()
    }
}
