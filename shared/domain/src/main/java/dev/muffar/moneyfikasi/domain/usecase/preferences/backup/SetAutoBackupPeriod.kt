package dev.muffar.moneyfikasi.domain.usecase.preferences.backup

import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.repository.BackupSettingsRepository

class SetAutoBackupPeriod(
    private val backupSettingsRepository: BackupSettingsRepository
) {
    suspend operator fun invoke(period: TimePeriod) {
        backupSettingsRepository.setAutoBackupPeriod(period)
    }
}