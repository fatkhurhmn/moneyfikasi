package dev.muffar.moneyfikasi.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SecurityPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    suspend fun enableAppLock(enable: Boolean) {
        dataStore.edit {
            it[APP_LOCK] = enable
        }
    }

    val isAppLockEnabled: Flow<Boolean> = dataStore.data.map {
        it[APP_LOCK] ?: false
    }

    suspend fun setAppLockPin(pin: String) {
        dataStore.edit {
            it[APP_LOCK_PIN] = pin
        }
    }

    val appLockPin: Flow<String> = dataStore.data.map {
        it[APP_LOCK_PIN] ?: ""
    }

    suspend fun enableBiometric(enable: Boolean) {
        dataStore.edit {
            it[BIOMETRIC_ENABLED] = enable
        }
    }

    val isBiometricEnabled: Flow<Boolean> = dataStore.data.map {
        it[BIOMETRIC_ENABLED] ?: false
    }

    companion object {
        val APP_LOCK = booleanPreferencesKey("app_lock")
        val APP_LOCK_PIN = stringPreferencesKey("app_lock_pin")
        val BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")
    }
}
