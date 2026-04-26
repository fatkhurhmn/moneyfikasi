package dev.muffar.moneyfikasi.domain.usecase.backup_restore

import dev.muffar.moneyfikasi.domain.model.LatestBackup
import dev.muffar.moneyfikasi.domain.repository.BackupRestoreRepository

class DeleteBackup(
    private val backupRestoreRepository: BackupRestoreRepository
) {
    suspend operator fun invoke(latestBackup: LatestBackup): Result<Unit> {
        return backupRestoreRepository.deleteBackup(latestBackup)
    }
}
