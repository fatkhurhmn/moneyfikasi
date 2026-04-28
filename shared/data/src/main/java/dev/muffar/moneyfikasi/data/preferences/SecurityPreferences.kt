package dev.muffar.moneyfikasi.data.preferences

import androidx.datastore.core.DataStore
import dev.muffar.moneyfikasi.domain.model.SecuritySettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SecurityPreferences @Inject constructor(
    private val dataStore: DataStore<SecuritySettings>,
) {
    suspend fun enableAppLock(enable: Boolean) {
        dataStore.updateData {
            it.copy(isAppLockEnabled = enable)
        }
    }

    val isAppLockEnabled: Flow<Boolean> = dataStore.data.map {
        it.isAppLockEnabled
    }

    suspend fun setAppLockPin(pin: String) {
        dataStore.updateData {
            it.copy(appLockPin = pin)
        }
    }

    val appLockPin: Flow<String> = dataStore.data.map {
        it.appLockPin
    }

    suspend fun enableBiometric(enable: Boolean) {
        dataStore.updateData {
            it.copy(isBiometricEnabled = enable)
        }
    }

    val isBiometricEnabled: Flow<Boolean> = dataStore.data.map {
        it.isBiometricEnabled
    }
}
