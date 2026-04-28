package dev.muffar.moneyfikasi.domain.usecase.preferences.backup

import dev.muffar.moneyfikasi.domain.model.LatestBackup
import dev.muffar.moneyfikasi.domain.repository.BackupSettingsRepository

class SetLatestBackup(
    private val backupSettingsRepository: BackupSettingsRepository
) {
    suspend operator fun invoke(latestBackup: LatestBackup) {
        backupSettingsRepository.setLatestBackup(latestBackup)
    }
}