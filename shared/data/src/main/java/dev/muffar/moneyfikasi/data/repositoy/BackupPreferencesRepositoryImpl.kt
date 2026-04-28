package dev.muffar.moneyfikasi.data.repositoy

import dev.muffar.moneyfikasi.data.preferences.AppPreferences
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.repository.BackupPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BackupPreferencesRepositoryImpl @Inject constructor(
    private val appPreferences: AppPreferences
) : BackupPreferencesRepository {
    override suspend fun setLatestBackup(fileName: String, date: Long, folder: String) {
        appPreferences.backup.setLatestBackup(fileName, date, folder)
    }

    override fun getLatestBackupName(): Flow<String> = appPreferences.backup.latestBackupName

    override fun getLatestBackupDate(): Flow<Long> = appPreferences.backup.latestBackupDate

    override fun getLatestBackupFolder(): Flow<String> = appPreferences.backup.latestBackupFolder

    override suspend fun setAutoBackupEnabled(isEnabled: Boolean) {
        appPreferences.backup.setAutoBackupEnabled(isEnabled)
    }

    override fun isAutoBackupEnabled(): Flow<Boolean> = appPreferences.backup.isAutoBackupEnabled

    override suspend fun setAutoBackupUri(uri: String) {
        appPreferences.backup.setAutoBackupUri(uri)
    }

    override fun getAutoBackupUri(): Flow<String> = appPreferences.backup.autoBackupUri

    override suspend fun setAutoBackupPeriod(period: TimePeriod) {
        appPreferences.backup.setAutoBackupPeriod(period)
    }

    override fun getAutoBackupPeriod(): Flow<TimePeriod> = appPreferences.backup.autoBackupPeriod

    override suspend fun setDeletePreviousBackup(isEnabled: Boolean) {
        appPreferences.backup.setDeletePreviousBackup(isEnabled)
    }

    override fun isDeletePreviousBackup(): Flow<Boolean> = appPreferences.backup.isDeletePreviousBackup
}
