package dev.muffar.moneyfikasi.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UiPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    suspend fun setBalanceVisibility(isVisible: Boolean) {
        dataStore.edit {
            it[BALANCE_VISIBILITY] = isVisible
        }
    }

    val isBalanceVisible: Flow<Boolean> = dataStore.data.map {
        it[BALANCE_VISIBILITY] ?: false
    }

    suspend fun setReportVisibility(isVisible: Boolean) {
        dataStore.edit {
            it[REPORT_VISIBILITY] = isVisible
        }
    }

    val isReportVisible: Flow<Boolean> = dataStore.data.map {
        it[REPORT_VISIBILITY] ?: false
    }

    companion object {
        val BALANCE_VISIBILITY = booleanPreferencesKey("balance_visibility")
        val REPORT_VISIBILITY = booleanPreferencesKey("report_visibility")
    }
}
