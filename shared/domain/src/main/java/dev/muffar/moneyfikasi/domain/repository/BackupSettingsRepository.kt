package dev.muffar.moneyfikasi.domain.repository

import dev.muffar.moneyfikasi.domain.model.BackupSettings
import dev.muffar.moneyfikasi.domain.model.LatestBackup
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import kotlinx.coroutines.flow.Flow

interface BackupSettingsRepository {
    fun getBackupSettings(): Flow<BackupSettings>
    suspend fun saveBackupSettings(settings: BackupSettings)

    suspend fun setLatestBackup(latestBackup: LatestBackup)
    suspend fun setAutoBackupEnabled(isEnabled: Boolean)
    suspend fun setAutoBackupUri(uri: String)
    suspend fun setAutoBackupPeriod(period: TimePeriod)
    suspend fun setDeletePreviousBackup(isEnabled: Boolean)
}