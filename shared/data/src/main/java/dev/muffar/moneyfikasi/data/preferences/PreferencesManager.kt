package dev.muffar.moneyfikasi.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class PreferencesManager @Inject constructor(
    private val datastore: DataStore<Preferences>
) {
    suspend fun setBalanceVisibility(isVisible: Boolean) {
        datastore.edit {
            it[BALANCE_VISIBILITY] = isVisible
        }
    }

    val isBalanceVisible = datastore.data.map {
        it[BALANCE_VISIBILITY] ?: false
    }

    suspend fun setReportVisibility(isVisible: Boolean) {
        datastore.edit {
            it[REPORT_VISIBILITY] = isVisible
        }
    }

    val isReportVisible = datastore.data.map {
        it[REPORT_VISIBILITY] ?: false
    }

    suspend fun setLatestBackup(fileName: String, date: Long, folder: String) {
        datastore.edit {
            it[LATEST_BACKUP_NAME] = fileName
            it[LATEST_BACKUP_DATE] = date
            it[LATEST_BACKUP_FOLDER] = folder
        }
    }

    val latestBackupName = datastore.data.map {
        it[LATEST_BACKUP_NAME] ?: ""
    }

    val latestBackupDate = datastore.data.map {
        it[LATEST_BACKUP_DATE] ?: 0L
    }

    val latestBackupFolder = datastore.data.map {
        it[LATEST_BACKUP_FOLDER] ?: ""
    }

    suspend fun setAutoBackupEnabled(isEnabled: Boolean) {
        datastore.edit {
            it[AUTO_BACKUP_ENABLED] = isEnabled
        }
    }

    val isAutoBackupEnabled = datastore.data.map {
        it[AUTO_BACKUP_ENABLED] ?: false
    }

    suspend fun setAutoBackupUri(uri: String) {
        datastore.edit {
            it[AUTO_BACKUP_URI] = uri
        }
    }

    val autoBackupUri = datastore.data.map {
        it[AUTO_BACKUP_URI] ?: ""
    }

    suspend fun setAutoBackupPeriod(period: TimePeriod) {
        datastore.edit {
            it[AUTO_BACKUP_PERIOD] = period.name
        }
    }

    val autoBackupPeriod = datastore.data.map {
        val periodName = it[AUTO_BACKUP_PERIOD] ?: TimePeriod.DAILY.name
        try {
            TimePeriod.valueOf(periodName)
        } catch (e: Exception) {
            TimePeriod.DAILY
        }
    }

    suspend fun setDeletePreviousBackup(isEnabled: Boolean) {
        datastore.edit {
            it[DELETE_PREVIOUS_BACKUP] = isEnabled
        }
    }

    val isDeletePreviousBackup = datastore.data.map {
        it[DELETE_PREVIOUS_BACKUP] ?: true
    }

    suspend fun enableAppLock(enable: Boolean) {
        datastore.edit {
            it[APP_LOCK] = enable
        }
    }

    val isAppLockEnabled = datastore.data.map {
        it[APP_LOCK] ?: false
    }

    suspend fun setAppLockPin(pin: String) {
        datastore.edit {
            it[APP_LOCK_PIN] = pin
        }
    }

    val appLockPin = datastore.data.map {
        it[APP_LOCK_PIN] ?: ""
    }

    companion object {
        val BALANCE_VISIBILITY = booleanPreferencesKey("balance_visibility")
        val REPORT_VISIBILITY = booleanPreferencesKey("report_visibility")
        val LATEST_BACKUP_NAME = stringPreferencesKey("latest_backup_name")
        val LATEST_BACKUP_DATE = longPreferencesKey("latest_backup_date")
        val LATEST_BACKUP_FOLDER = stringPreferencesKey("latest_backup_folder")
        val AUTO_BACKUP_ENABLED = booleanPreferencesKey("auto_backup_enabled")
        val AUTO_BACKUP_URI = stringPreferencesKey("auto_backup_uri")
        val AUTO_BACKUP_PERIOD = stringPreferencesKey("auto_backup_period")
        val DELETE_PREVIOUS_BACKUP = booleanPreferencesKey("delete_previous_backup")
        val APP_LOCK = booleanPreferencesKey("app_lock")
        val APP_LOCK_PIN = stringPreferencesKey("app_lock_pin")
    }
}
