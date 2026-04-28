package dev.muffar.moneyfikasi.data.preferences

import androidx.datastore.core.DataStore
import dev.muffar.moneyfikasi.domain.model.SecuritySettings
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SecurityPreferences @Inject constructor(
    private val dataStore: DataStore<SecuritySettings>,
) {
    val securitySettings: Flow<SecuritySettings> = dataStore.data

    suspend fun enableAppLock(enable: Boolean) {
        dataStore.updateData {
            it.copy(isAppLockEnabled = enable)
        }
    }

    suspend fun setAppLockPin(pin: String) {
        dataStore.updateData {
            it.copy(appLockPin = pin)
        }
    }

    suspend fun enableBiometric(enable: Boolean) {
        dataStore.updateData {
            it.copy(isBiometricEnabled = enable)
        }
    }
}
