package dev.muffar.moneyfikasi.domain.usecase.backup_restore

import dev.muffar.moneyfikasi.domain.model.LatestBackup
import dev.muffar.moneyfikasi.domain.repository.BackupPreferencesRepository
import dev.muffar.moneyfikasi.domain.repository.BackupRestoreRepository

class DeleteLatestBackup(
    private val backupRestoreRepository: BackupRestoreRepository,
    private val backupPreferencesRepository: BackupPreferencesRepository
) {
    suspend operator fun invoke(latestBackup: LatestBackup): Result<Unit> {
        if (latestBackup.name.isEmpty() || latestBackup.folder.isEmpty()) {
            return Result.failure(Exception("No backup to delete"))
        }

        return backupRestoreRepository.deleteBackup(latestBackup)
            .onSuccess {
                backupPreferencesRepository.setLatestBackup("", 0L, "")
            }
    }
}
