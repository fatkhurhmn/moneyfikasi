package dev.muffar.moneyfikasi.domain.usecase.preferences.backup

data class BackupSettingsUseCases(
    val getLatestBackup: GetLatestBackup,
    val setLatestBackup: SetLatestBackup,
    val isAutoBackupEnabled: IsAutoBackupEnabled,
    val setAutoBackupEnabled: SetAutoBackupEnabled,
    val getAutoBackupUri: GetAutoBackupUri,
    val setAutoBackupUri: SetAutoBackupUri,
    val getAutoBackupPeriod: GetAutoBackupPeriod,
    val setAutoBackupPeriod: SetAutoBackupPeriod,
    val isDeletePreviousBackup: IsDeletePreviousBackup,
    val setDeletePreviousBackup: SetDeletePreviousBackup
)
