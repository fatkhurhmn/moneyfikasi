package dev.muffar.moneyfikasi.domain.usecase.backup_restore

import dev.muffar.moneyfikasi.domain.repository.BackupRestoreRepository

class BackupData(
    private val backupRestoreRepository: BackupRestoreRepository
) {

    operator fun invoke():Int {
        return backupRestoreRepository.backupData()
    }
}