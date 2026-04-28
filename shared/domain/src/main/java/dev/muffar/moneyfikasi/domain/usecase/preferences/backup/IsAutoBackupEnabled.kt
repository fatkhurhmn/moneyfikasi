package dev.muffar.moneyfikasi.domain.usecase.preferences.backup

import dev.muffar.moneyfikasi.domain.repository.BackupSettingsRepository

class IsAutoBackupEnabled(
    private val backupSettingsRepository: BackupSettingsRepository
) {
    operator fun invoke() = backupSettingsRepository.isAutoBackupEnabled()
}