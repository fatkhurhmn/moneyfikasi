package dev.muffar.moneyfikasi.data.repositoy

import dev.muffar.moneyfikasi.data.preferences.PreferencesManager
import dev.muffar.moneyfikasi.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow

class PreferencesRepositoryImpl(
    private val preferencesManager: PreferencesManager
) : PreferencesRepository {
    override suspend fun setBalanceVisibility(isVisible: Boolean) {
        preferencesManager.setBalanceVisibility(isVisible)
    }

    override fun isBalanceVisible(): Flow<Boolean> = preferencesManager.isBalanceVisible

    override suspend fun setReportVisibility(isVisible: Boolean) {
        preferencesManager.setReportVisibility(isVisible)
    }

    override fun isReportVisible(): Flow<Boolean> = preferencesManager.isReportVisible

    override suspend fun setLatestBackup(fileName: String, date: Long) {
        preferencesManager.setLatestBackup(fileName, date)
    }

    override fun getLatestBackupName(): Flow<String> = preferencesManager.latestBackupName

    override fun getLatestBackupDate(): Flow<Long> = preferencesManager.latestBackupDate

    override suspend fun setAutoBackupEnabled(isEnabled: Boolean) {
        preferencesManager.setAutoBackupEnabled(isEnabled)
    }

    override fun isAutoBackupEnabled(): Flow<Boolean> = preferencesManager.isAutoBackupEnabled

    override suspend fun setAutoBackupUri(uri: String) {
        preferencesManager.setAutoBackupUri(uri)
    }

    override fun getAutoBackupUri(): Flow<String> = preferencesManager.autoBackupUri

    override suspend fun setAutoBackupPeriod(period: String) {
        preferencesManager.setAutoBackupPeriod(period)
    }

    override fun getAutoBackupPeriod(): Flow<String> = preferencesManager.autoBackupPeriod
}