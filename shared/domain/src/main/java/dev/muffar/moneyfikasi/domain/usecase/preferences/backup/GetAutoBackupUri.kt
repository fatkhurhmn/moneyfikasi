package dev.muffar.moneyfikasi.domain.usecase.preferences.backup

import dev.muffar.moneyfikasi.domain.repository.BackupSettingsRepository

class GetAutoBackupUri(
    private val backupSettingsRepository: BackupSettingsRepository
) {
    operator fun invoke() = backupSettingsRepository.getAutoBackupUri()
}