package dev.muffar.moneyfikasi.domain.usecase.preferences.backup

import dev.muffar.moneyfikasi.domain.repository.BackupSettingsRepository

class SetAutoBackupUri(
    private val backupSettingsRepository: BackupSettingsRepository
) {
    suspend operator fun invoke(uri: String) {
        backupSettingsRepository.setAutoBackupUri(uri)
    }
}