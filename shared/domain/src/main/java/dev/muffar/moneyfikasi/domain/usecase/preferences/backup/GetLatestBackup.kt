package dev.muffar.moneyfikasi.domain.usecase.preferences.backup

import dev.muffar.moneyfikasi.domain.model.LatestBackup
import dev.muffar.moneyfikasi.domain.repository.BackupSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetLatestBackup(
    private val backupSettingsRepository: BackupSettingsRepository
) {
    operator fun invoke(): Flow<LatestBackup> {
        return combine(
            backupSettingsRepository.getLatestBackupName(),
            backupSettingsRepository.getLatestBackupDate(),
            backupSettingsRepository.getLatestBackupFolder()
        ) { name, date, folder ->
            LatestBackup(name, date, folder)
        }
    }
}
