package dev.muffar.moneyfikasi.domain.usecase.preferences

import dev.muffar.moneyfikasi.domain.repository.BackupPreferencesRepository

class GetAutoBackupPeriod(
    private val backupPreferencesRepository: BackupPreferencesRepository
) {
    operator fun invoke() = backupPreferencesRepository.getAutoBackupPeriod()
}