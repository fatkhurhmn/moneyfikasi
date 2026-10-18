package dev.muffar.moneyfikasi.domain.usecase.drive

import dev.muffar.moneyfikasi.domain.repository.DriveBackupRepository

class GetDriveAccountEmail(
    private val repository: DriveBackupRepository
) {

    operator fun invoke(): String {
        return repository.getSignedInAccountEmail()
    }
}
