package dev.muffar.moneyfikasi.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BackupPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    suspend fun setLatestBackup(fileName: String, date: Long, folder: String) {
        dataStore.edit {
            it[LATEST_BACKUP_NAME] = fileName
            it[LATEST_BACKUP_DATE] = date
            it[LATEST_BACKUP_FOLDER] = folder
        }
    }

    val latestBackupName: Flow<String> = dataStore.data.map {
        it[LATEST_BACKUP_NAME] ?: ""
    }

    val latestBackupDate: Flow<Long> = dataStore.data.map {
        it[LATEST_BACKUP_DATE] ?: 0L
    }

    val latestBackupFolder: Flow<String> = dataStore.data.map {
        it[LATEST_BACKUP_FOLDER] ?: ""
    }

    suspend fun setAutoBackupEnabled(isEnabled: Boolean) {
        dataStore.edit {
            it[AUTO_BACKUP_ENABLED] = isEnabled
        }
    }

    val isAutoBackupEnabled: Flow<Boolean> = dataStore.data.map {
        it[AUTO_BACKUP_ENABLED] ?: false
    }

    suspend fun setAutoBackupUri(uri: String) {
        dataStore.edit {
            it[AUTO_BACKUP_URI] = uri
        }
    }

    val autoBackupUri: Flow<String> = dataStore.data.map {
        it[AUTO_BACKUP_URI] ?: ""
    }

    suspend fun setAutoBackupPeriod(period: TimePeriod) {
        dataStore.edit {
            it[AUTO_BACKUP_PERIOD] = period.name
        }
    }

    val autoBackupPeriod: Flow<TimePeriod> = dataStore.data.map {
        val periodName = it[AUTO_BACKUP_PERIOD] ?: TimePeriod.DAILY.name
        try {
            TimePeriod.valueOf(periodName)
        } catch (e: Exception) {
            e.printStackTrace()
            TimePeriod.DAILY
        }
    }

    suspend fun setDeletePreviousBackup(isEnabled: Boolean) {
        dataStore.edit {
            it[DELETE_PREVIOUS_BACKUP] = isEnabled
        }
    }

    val isDeletePreviousBackup: Flow<Boolean> = dataStore.data.map {
        it[DELETE_PREVIOUS_BACKUP] ?: true
    }

    companion object {
        val LATEST_BACKUP_NAME = stringPreferencesKey("latest_backup_name")
        val LATEST_BACKUP_DATE = longPreferencesKey("latest_backup_date")
        val LATEST_BACKUP_FOLDER = stringPreferencesKey("latest_backup_folder")
        val AUTO_BACKUP_ENABLED = booleanPreferencesKey("auto_backup_enabled")
        val AUTO_BACKUP_URI = stringPreferencesKey("auto_backup_uri")
        val AUTO_BACKUP_PERIOD = stringPreferencesKey("auto_backup_period")
        val DELETE_PREVIOUS_BACKUP = booleanPreferencesKey("delete_previous_backup")
    }
}
