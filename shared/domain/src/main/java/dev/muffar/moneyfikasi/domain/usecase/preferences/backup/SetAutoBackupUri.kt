package dev.muffar.moneyfikasi.domain.usecase.preferences.backup

import dev.muffar.moneyfikasi.domain.repository.BackupPreferencesRepository

class SetAutoBackupUri(
    private val backupPreferencesRepository: BackupPreferencesRepository
) {
    suspend operator fun invoke(uri: String) {
        backupPreferencesRepository.setAutoBackupUri(uri)
    }
}