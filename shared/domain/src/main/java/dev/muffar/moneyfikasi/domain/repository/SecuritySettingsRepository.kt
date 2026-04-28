package dev.muffar.moneyfikasi.domain.repository

import dev.muffar.moneyfikasi.domain.model.SecuritySettings
import kotlinx.coroutines.flow.Flow

interface SecuritySettingsRepository {
    fun getSecuritySettings(): Flow<SecuritySettings>
    suspend fun enableAppLock(enable: Boolean)
    suspend fun setAppLockPin(pin: String)
    suspend fun enableBiometric(enable: Boolean)
}
