package dev.muffar.moneyfikasi.domain.repository

import dev.muffar.moneyfikasi.domain.model.TimePeriod
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    suspend fun setBalanceVisibility(isVisible: Boolean)
    fun isBalanceVisible(): Flow<Boolean>
    suspend fun setReportVisibility(isVisible: Boolean)
    fun isReportVisible(): Flow<Boolean>
    suspend fun setLatestBackup(fileName: String, date: Long, folder: String)
    fun getLatestBackupName(): Flow<String>
    fun getLatestBackupDate(): Flow<Long>
    fun getLatestBackupFolder(): Flow<String>
    suspend fun setAutoBackupEnabled(isEnabled: Boolean)
    fun isAutoBackupEnabled(): Flow<Boolean>
    suspend fun setAutoBackupUri(uri: String)
    fun getAutoBackupUri(): Flow<String>
    suspend fun setAutoBackupPeriod(period: TimePeriod)
    fun getAutoBackupPeriod(): Flow<TimePeriod>
    suspend fun setDeletePreviousBackup(isEnabled: Boolean)
    fun isDeletePreviousBackup(): Flow<Boolean>
}
