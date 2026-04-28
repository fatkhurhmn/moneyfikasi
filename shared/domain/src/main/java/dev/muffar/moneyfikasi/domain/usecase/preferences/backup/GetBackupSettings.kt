package dev.muffar.moneyfikasi.domain.usecase.preferences.backup

import dev.muffar.moneyfikasi.domain.model.BackupSettings
import dev.muffar.moneyfikasi.domain.repository.BackupSettingsRepository
import kotlinx.coroutines.flow.Flow

class GetBackupSettings(
    private val backupSettingsRepository: BackupSettingsRepository
) {
    operator fun invoke(): Flow<BackupSettings> = backupSettingsRepository.getBackupSettings()
}