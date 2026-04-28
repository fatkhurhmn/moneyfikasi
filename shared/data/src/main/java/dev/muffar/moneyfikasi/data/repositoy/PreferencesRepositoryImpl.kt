package dev.muffar.moneyfikasi.data.repositoy

import dev.muffar.moneyfikasi.data.preferences.AppPreferences
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val appPreferences: AppPreferences
) : PreferencesRepository {
    override suspend fun setBalanceVisibility(isVisible: Boolean) {
        appPreferences.ui.setBalanceVisibility(isVisible)
    }

    override fun isBalanceVisible(): Flow<Boolean> = appPreferences.ui.isBalanceVisible

    override suspend fun setReportVisibility(isVisible: Boolean) {
        appPreferences.ui.setReportVisibility(isVisible)
    }

    override fun isReportVisible(): Flow<Boolean> = appPreferences.ui.isReportVisible

    override suspend fun setLatestBackup(fileName: String, date: Long, folder: String) {
        appPreferences.backup.setLatestBackup(fileName, date, folder)
    }

    override fun getLatestBackupName(): Flow<String> = appPreferences.backup.latestBackupName

    override fun getLatestBackupDate(): Flow<Long> = appPreferences.backup.latestBackupDate

    override fun getLatestBackupFolder(): Flow<String> = appPreferences.backup.latestBackupFolder

    override suspend fun setAutoBackupEnabled(isEnabled: Boolean) {
        appPreferences.backup.setAutoBackupEnabled(isEnabled)
    }

    override fun isAutoBackupEnabled(): Flow<Boolean> = appPreferences.backup.isAutoBackupEnabled

    override suspend fun setAutoBackupUri(uri: String) {
        appPreferences.backup.setAutoBackupUri(uri)
    }

    override fun getAutoBackupUri(): Flow<String> = appPreferences.backup.autoBackupUri

    override suspend fun setAutoBackupPeriod(period: TimePeriod) {
        appPreferences.backup.setAutoBackupPeriod(period)
    }

    override fun getAutoBackupPeriod(): Flow<TimePeriod> = appPreferences.backup.autoBackupPeriod

    override suspend fun setDeletePreviousBackup(isEnabled: Boolean) {
        appPreferences.backup.setDeletePreviousBackup(isEnabled)
    }

    override fun isDeletePreviousBackup(): Flow<Boolean> = appPreferences.backup.isDeletePreviousBackup

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
