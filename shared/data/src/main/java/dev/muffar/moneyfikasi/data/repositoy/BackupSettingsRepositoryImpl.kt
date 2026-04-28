package dev.muffar.moneyfikasi.data.repositoy

import dev.muffar.moneyfikasi.data.preferences.BackupPreferences
import dev.muffar.moneyfikasi.domain.model.BackupSettings
import dev.muffar.moneyfikasi.domain.model.LatestBackup
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.repository.BackupSettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BackupSettingsRepositoryImpl @Inject constructor(
    private val backupPreferences: BackupPreferences
) : BackupSettingsRepository {

    override fun getBackupSettings(): Flow<BackupSettings> = backupPreferences.backupSettings

    override suspend fun saveBackupSettings(settings: BackupSettings) {
        backupPreferences.saveBackupSettings(settings)
    }

    override suspend fun setLatestBackup(latestBackup: LatestBackup) {
        backupPreferences.setLatestBackup(latestBackup)
    }

    override suspend fun setAutoBackupEnabled(isEnabled: Boolean) {
        backupPreferences.setAutoBackupEnabled(isEnabled)
    }

    override suspend fun setAutoBackupUri(uri: String) {
        backupPreferences.setAutoBackupUri(uri)
    }

    override suspend fun setAutoBackupPeriod(period: TimePeriod) {
        backupPreferences.setAutoBackupPeriod(period)
    }

    override suspend fun setDeletePreviousBackup(isEnabled: Boolean) {
        backupPreferences.setDeletePreviousBackup(isEnabled)
    }
}