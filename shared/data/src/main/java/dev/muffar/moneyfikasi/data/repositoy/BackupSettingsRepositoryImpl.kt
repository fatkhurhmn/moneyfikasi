package dev.muffar.moneyfikasi.data.repositoy

import dev.muffar.moneyfikasi.data.preferences.BackupPreferences
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.repository.BackupSettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BackupSettingsRepositoryImpl @Inject constructor(
    private val backupPreferences: BackupPreferences
) : BackupSettingsRepository {
    override suspend fun setLatestBackup(fileName: String, date: Long, folder: String) {
        backupPreferences.setLatestBackup(fileName, date, folder)
    }

    override fun getLatestBackupName(): Flow<String> = backupPreferences.latestBackupName

    override fun getLatestBackupDate(): Flow<Long> = backupPreferences.latestBackupDate

    override fun getLatestBackupFolder(): Flow<String> = backupPreferences.latestBackupFolder

    override suspend fun setAutoBackupEnabled(isEnabled: Boolean) {
        backupPreferences.setAutoBackupEnabled(isEnabled)
    }

    override fun isAutoBackupEnabled(): Flow<Boolean> = backupPreferences.isAutoBackupEnabled

    override suspend fun setAutoBackupUri(uri: String) {
        backupPreferences.setAutoBackupUri(uri)
    }

    override fun getAutoBackupUri(): Flow<String> = backupPreferences.autoBackupUri

    override suspend fun setAutoBackupPeriod(period: TimePeriod) {
        backupPreferences.setAutoBackupPeriod(period)
    }

    override fun getAutoBackupPeriod(): Flow<TimePeriod> = backupPreferences.autoBackupPeriod

    override suspend fun setDeletePreviousBackup(isEnabled: Boolean) {
        backupPreferences.setDeletePreviousBackup(isEnabled)
    }

    override fun isDeletePreviousBackup(): Flow<Boolean> = backupPreferences.isDeletePreviousBackup
}
