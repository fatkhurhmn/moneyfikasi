package dev.muffar.moneyfikasi.domain.usecase.backup_restore

import dev.muffar.moneyfikasi.domain.repository.BackupRestoreRepository

class CancelDriveAutoBackup(
    private val backupRestoreRepository: BackupRestoreRepository
) {

    operator fun invoke() {
        backupRestoreRepository.cancelDriveAutoBackup()
    }
}
