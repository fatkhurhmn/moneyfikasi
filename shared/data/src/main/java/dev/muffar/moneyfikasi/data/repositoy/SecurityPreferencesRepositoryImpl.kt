package dev.muffar.moneyfikasi.data.repositoy

import dev.muffar.moneyfikasi.data.preferences.AppPreferences
import dev.muffar.moneyfikasi.domain.repository.SecurityPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SecurityPreferencesRepositoryImpl @Inject constructor(
    private val appPreferences: AppPreferences
) : SecurityPreferencesRepository {
    override suspend fun enableAppLock(enable: Boolean) {
        appPreferences.security.enableAppLock(enable)
    }

    override fun isAppLockEnabled(): Flow<Boolean> = appPreferences.security.isAppLockEnabled

    override suspend fun setAppLockPin(pin: String) {
        appPreferences.security.setAppLockPin(pin)
    }

    override fun getAppLockPin(): Flow<String> = appPreferences.security.appLockPin

    override suspend fun enableBiometric(enable: Boolean) {
        appPreferences.security.enableBiometric(enable)
    }

    override fun isBiometricEnabled(): Flow<Boolean> = appPreferences.security.isBiometricEnabled
}
