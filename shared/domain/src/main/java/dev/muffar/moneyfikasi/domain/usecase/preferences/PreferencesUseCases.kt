package dev.muffar.moneyfikasi.domain.usecase.preferences

data class PreferencesUseCases(
    val isBalanceVisible: IsBalanceVisible,
    val setBalanceVisibility: SetBalanceVisibility,
    val isReportVisible: IsReportVisible,
    val setReportVisibility: SetReportVisibility,
    val getLatestBackup: GetLatestBackup,
    val setLatestBackup: SetLatestBackup,
    val isAutoBackupEnabled: IsAutoBackupEnabled,
    val setAutoBackupEnabled: SetAutoBackupEnabled,
    val getAutoBackupUri: GetAutoBackupUri,
    val setAutoBackupUri: SetAutoBackupUri,
    val getAutoBackupPeriod: GetAutoBackupPeriod,
    val setAutoBackupPeriod: SetAutoBackupPeriod,
    val isDeletePreviousBackup: IsDeletePreviousBackup,
    val setDeletePreviousBackup: SetDeletePreviousBackup,
    val getAppLockType: GetAppLockType,
    val setAppLockType: SetAppLockType,
    val getAppLockPin: GetAppLockPin,
    val setAppLockPin: SetAppLockPin
)
