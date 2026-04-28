package dev.muffar.moneyfikasi.domain.usecase.preferences.backup

import dev.muffar.moneyfikasi.domain.repository.BackupSettingsRepository

class GetAutoBackupPeriod(
    private val backupSettingsRepository: BackupSettingsRepository
) {
    operator fun invoke() = backupSettingsRepository.getAutoBackupPeriod()
}