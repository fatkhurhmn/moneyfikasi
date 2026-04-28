package dev.muffar.moneyfikasi.data.preferences

import androidx.datastore.core.DataStore
import dev.muffar.moneyfikasi.domain.model.BackupSettings
import dev.muffar.moneyfikasi.domain.model.LatestBackup
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BackupPreferences @Inject constructor(
    private val dataStore: DataStore<BackupSettings>,
) {
    val backupSettings: Flow<BackupSettings> = dataStore.data

    suspend fun saveBackupSettings(settings: BackupSettings) {
        dataStore.updateData { settings }
    }

    suspend fun setLatestBackup(latestBackup: LatestBackup) {
        dataStore.updateData { it.copy(latestBackup = latestBackup) }
    }

    suspend fun setAutoBackupEnabled(isEnabled: Boolean) {
        dataStore.updateData {
            it.copy(autoBackup = it.autoBackup.copy(isEnabled = isEnabled))
        }
    }

    suspend fun setAutoBackupUri(uri: String) {
        dataStore.updateData {
            it.copy(autoBackup = it.autoBackup.copy(uri = uri))
        }
    }

    suspend fun setAutoBackupPeriod(period: TimePeriod) {
        dataStore.updateData {
            it.copy(autoBackup = it.autoBackup.copy(period = period.name))
        }
    }

    suspend fun setDeletePreviousBackup(isEnabled: Boolean) {
        dataStore.updateData {
            it.copy(isDeletePreviousBackup = isEnabled)
        }
    }
}