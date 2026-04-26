package dev.muffar.moneyfikasi.domain.usecase.backup_restore

import dev.muffar.moneyfikasi.domain.model.LatestBackup
import dev.muffar.moneyfikasi.domain.repository.BackupRestoreRepository
import dev.muffar.moneyfikasi.domain.repository.PreferencesRepository

class DeleteLatestBackup(
    private val backupRestoreRepository: BackupRestoreRepository,
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(latestBackup: LatestBackup): Result<Unit> {
        if (latestBackup.name.isEmpty() || latestBackup.folder.isEmpty()) {
            return Result.failure(Exception("No backup to delete"))
        }

        return backupRestoreRepository.deleteBackup(latestBackup)
            .onSuccess {
                preferencesRepository.setLatestBackup("", 0L, "")
            }
    }
}
