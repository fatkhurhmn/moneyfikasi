package dev.muffar.moneyfikasi.domain.repository

import kotlinx.coroutines.flow.Flow

interface SecurityPreferencesRepository {
    suspend fun enableAppLock(enable: Boolean)
    fun isAppLockEnabled(): Flow<Boolean>
    suspend fun setAppLockPin(pin: String)
    fun getAppLockPin(): Flow<String>
    suspend fun enableBiometric(enable: Boolean)
    fun isBiometricEnabled(): Flow<Boolean>
}
