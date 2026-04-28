package dev.muffar.moneyfikasi.domain.usecase.preferences.backup

import dev.muffar.moneyfikasi.domain.repository.BackupPreferencesRepository

class SetAutoBackupEnabled(
    private val backupPreferencesRepository: BackupPreferencesRepository
) {
    suspend operator fun invoke(isEnabled: Boolean) {
        backupPreferencesRepository.setAutoBackupEnabled(isEnabled)
    }
}