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
}