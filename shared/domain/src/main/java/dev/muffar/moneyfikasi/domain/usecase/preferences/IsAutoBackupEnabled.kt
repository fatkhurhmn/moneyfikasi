package dev.muffar.moneyfikasi.domain.usecase.preferences

import dev.muffar.moneyfikasi.domain.repository.BackupPreferencesRepository

class IsAutoBackupEnabled(
    private val backupPreferencesRepository: BackupPreferencesRepository
) {
    operator fun invoke() = backupPreferencesRepository.isAutoBackupEnabled()
}