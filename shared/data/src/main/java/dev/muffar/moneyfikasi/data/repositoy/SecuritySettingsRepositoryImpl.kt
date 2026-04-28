package dev.muffar.moneyfikasi.data.repositoy

import dev.muffar.moneyfikasi.data.preferences.SecurityPreferences
import dev.muffar.moneyfikasi.domain.model.SecuritySettings
import dev.muffar.moneyfikasi.domain.repository.SecuritySettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SecuritySettingsRepositoryImpl @Inject constructor(
    private val securityPreferences: SecurityPreferences
) : SecuritySettingsRepository {
    override fun getSecuritySettings(): Flow<SecuritySettings> {
        return securityPreferences.securitySettings
    }

    override suspend fun enableAppLock(enable: Boolean) {
        securityPreferences.enableAppLock(enable)
    }

    override suspend fun setAppLockPin(pin: String) {
        securityPreferences.setAppLockPin(pin)
    }

    override suspend fun enableBiometric(enable: Boolean) {
        securityPreferences.enableBiometric(enable)
    }
}
