package dev.muffar.moneyfikasi.domain.usecase.preferences.backup

import dev.muffar.moneyfikasi.domain.repository.BackupSettingsRepository

class SetLatestBackup(
    private val backupSettingsRepository: BackupSettingsRepository
) {
    suspend operator fun invoke(fileName: String, date: Long, folder: String) {
        backupSettingsRepository.setLatestBackup(fileName, date, folder)
    }
}