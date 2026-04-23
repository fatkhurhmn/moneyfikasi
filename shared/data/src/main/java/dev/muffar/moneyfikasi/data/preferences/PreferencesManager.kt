package dev.muffar.moneyfikasi.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
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

    suspend fun setLatestBackup(fileName: String, date: Long) {
        datastore.edit {
            it[LATEST_BACKUP_NAME] = fileName
            it[LATEST_BACKUP_DATE] = date
        }
    }

    val latestBackupName = datastore.data.map {
        it[LATEST_BACKUP_NAME] ?: ""
    }

    val latestBackupDate = datastore.data.map {
        it[LATEST_BACKUP_DATE] ?: 0L
    }

    companion object {
        val BALANCE_VISIBILITY = booleanPreferencesKey("balance_visibility")
        val REPORT_VISIBILITY = booleanPreferencesKey("report_visibility")
        val LATEST_BACKUP_NAME = stringPreferencesKey("latest_backup_name")
        val LATEST_BACKUP_DATE = longPreferencesKey("latest_backup_date")
    }
}
