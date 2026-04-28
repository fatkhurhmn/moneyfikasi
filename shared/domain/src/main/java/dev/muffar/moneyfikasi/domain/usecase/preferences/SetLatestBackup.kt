package dev.muffar.moneyfikasi.domain.usecase.preferences

import dev.muffar.moneyfikasi.domain.repository.BackupPreferencesRepository

class SetLatestBackup(
    private val backupPreferencesRepository: BackupPreferencesRepository
) {
    suspend operator fun invoke(fileName: String, date: Long, folder: String) {
        backupPreferencesRepository.setLatestBackup(fileName, date, folder)
    }
}