package dev.muffar.moneyfikasi.data.repositoy

import dev.muffar.moneyfikasi.data.preferences.SecurityPreferences
import dev.muffar.moneyfikasi.domain.repository.SecuritySettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SecuritySettingsRepositoryImpl @Inject constructor(
    private val securityPreferences: SecurityPreferences
) : SecuritySettingsRepository {
    override suspend fun enableAppLock(enable: Boolean) {
        securityPreferences.enableAppLock(enable)
    }

    override fun isAppLockEnabled(): Flow<Boolean> = securityPreferences.isAppLockEnabled

    override suspend fun setAppLockPin(pin: String) {
        securityPreferences.setAppLockPin(pin)
    }

    override fun getAppLockPin(): Flow<String> = securityPreferences.appLockPin

    override suspend fun enableBiometric(enable: Boolean) {
        securityPreferences.enableBiometric(enable)
    }

    override fun isBiometricEnabled(): Flow<Boolean> = securityPreferences.isBiometricEnabled
}
