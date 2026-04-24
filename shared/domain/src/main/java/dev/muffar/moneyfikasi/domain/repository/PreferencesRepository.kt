package dev.muffar.moneyfikasi.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    suspend fun setBalanceVisibility(isVisible: Boolean)
    fun isBalanceVisible(): Flow<Boolean>
    suspend fun setReportVisibility(isVisible: Boolean)
    fun isReportVisible(): Flow<Boolean>
    suspend fun setLatestBackup(fileName: String, date: Long)
    fun getLatestBackupName(): Flow<String>
    fun getLatestBackupDate(): Flow<Long>
    suspend fun setAutoBackupEnabled(isEnabled: Boolean)
    fun isAutoBackupEnabled(): Flow<Boolean>
    suspend fun setAutoBackupUri(uri: String)
    fun getAutoBackupUri(): Flow<String>
    suspend fun setAutoBackupPeriod(period: String)
    fun getAutoBackupPeriod(): Flow<String>
}