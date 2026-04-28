package dev.muffar.moneyfikasi.domain.usecase.preferences.backup

data class BackupSettingsUseCases(
    val getBackupSettings: GetBackupSettings,
    val setLatestBackup: SetLatestBackup,
    val setAutoBackupEnabled: SetAutoBackupEnabled,
    val setAutoBackupUri: SetAutoBackupUri,
    val setAutoBackupPeriod: SetAutoBackupPeriod,
    val setDeletePreviousBackup: SetDeletePreviousBackup
)