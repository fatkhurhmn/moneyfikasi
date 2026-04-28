package dev.muffar.moneyfikasi.domain.usecase.preferences.backup

import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.repository.BackupPreferencesRepository

class SetAutoBackupPeriod(
    private val backupPreferencesRepository: BackupPreferencesRepository
) {
    suspend operator fun invoke(period: TimePeriod) {
        backupPreferencesRepository.setAutoBackupPeriod(period)
    }
}