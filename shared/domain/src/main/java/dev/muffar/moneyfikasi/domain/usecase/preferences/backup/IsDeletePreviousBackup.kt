package dev.muffar.moneyfikasi.domain.usecase.preferences.backup

import dev.muffar.moneyfikasi.domain.repository.BackupSettingsRepository

class IsDeletePreviousBackup(
    private val backupSettingsRepository: BackupSettingsRepository
) {
    operator fun invoke() = backupSettingsRepository.isDeletePreviousBackup()
}