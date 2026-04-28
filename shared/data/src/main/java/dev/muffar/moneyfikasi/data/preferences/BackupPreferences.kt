package dev.muffar.moneyfikasi.data.preferences

import androidx.datastore.core.DataStore
import dev.muffar.moneyfikasi.domain.model.BackupSettings
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BackupPreferences @Inject constructor(
    private val dataStore: DataStore<BackupSettings>,
) {
    suspend fun setLatestBackup(fileName: String, date: Long, folder: String) {
        dataStore.updateData {
            it.copy(
                latestBackupName = fileName,
                latestBackupDate = date,
                latestBackupFolder = folder
            )
        }
    }

    val latestBackupName: Flow<String> = dataStore.data.map {
        it.latestBackupName
    }

    val latestBackupDate: Flow<Long> = dataStore.data.map {
        it.latestBackupDate
    }

    val latestBackupFolder: Flow<String> = dataStore.data.map {
        it.latestBackupFolder
    }

    suspend fun setAutoBackupEnabled(isEnabled: Boolean) {
        dataStore.updateData {
            it.copy(isAutoBackupEnabled = isEnabled)
        }
    }

    val isAutoBackupEnabled: Flow<Boolean> = dataStore.data.map {
        it.isAutoBackupEnabled
    }

    suspend fun setAutoBackupUri(uri: String) {
        dataStore.updateData {
            it.copy(autoBackupUri = uri)
        }
    }

    val autoBackupUri: Flow<String> = dataStore.data.map {
        it.autoBackupUri
    }

    suspend fun setAutoBackupPeriod(period: TimePeriod) {
        dataStore.updateData {
            it.copy(autoBackupPeriod = period.name)
        }
    }

    val autoBackupPeriod: Flow<TimePeriod> = dataStore.data.map {
        try {
            TimePeriod.valueOf(it.autoBackupPeriod)
        } catch (_: Exception) {
            TimePeriod.DAILY
        }
    }

    suspend fun setDeletePreviousBackup(isEnabled: Boolean) {
        dataStore.updateData {
            it.copy(isDeletePreviousBackup = isEnabled)
        }
    }

    val isDeletePreviousBackup: Flow<Boolean> = dataStore.data.map {
        it.isDeletePreviousBackup
    }
}
