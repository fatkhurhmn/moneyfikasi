package dev.muffar.moneyfikasi.domain.usecase.preferences.backup

import dev.muffar.moneyfikasi.domain.repository.BackupSettingsRepository

class SetAutoBackupEnabled(
    private val backupSettingsRepository: BackupSettingsRepository
) {
    suspend operator fun invoke(isEnabled: Boolean) {
        backupSettingsRepository.setAutoBackupEnabled(isEnabled)
    }
}