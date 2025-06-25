package dev.muffar.moneyfikasi.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
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

    companion object {
        val BALANCE_VISIBILITY = booleanPreferencesKey("balance_visibility")
    }
}
