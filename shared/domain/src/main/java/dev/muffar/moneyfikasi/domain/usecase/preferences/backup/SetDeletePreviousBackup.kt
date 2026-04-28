package dev.muffar.moneyfikasi.domain.usecase.preferences.backup

import dev.muffar.moneyfikasi.domain.repository.BackupPreferencesRepository

class SetDeletePreviousBackup(
    private val backupPreferencesRepository: BackupPreferencesRepository
) {
    suspend operator fun invoke(isEnabled: Boolean) {
        backupPreferencesRepository.setDeletePreviousBackup(isEnabled)
    }
}