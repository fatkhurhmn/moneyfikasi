package dev.muffar.moneyfikasi.domain.usecase.preferences

import dev.muffar.moneyfikasi.domain.model.LatestBackup
import dev.muffar.moneyfikasi.domain.repository.BackupPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetLatestBackup(
    private val backupPreferencesRepository: BackupPreferencesRepository
) {
    operator fun invoke(): Flow<LatestBackup> {
        return combine(
            backupPreferencesRepository.getLatestBackupName(),
            backupPreferencesRepository.getLatestBackupDate(),
            backupPreferencesRepository.getLatestBackupFolder()
        ) { name, date, folder ->
            LatestBackup(name, date, folder)
        }
    }
}
