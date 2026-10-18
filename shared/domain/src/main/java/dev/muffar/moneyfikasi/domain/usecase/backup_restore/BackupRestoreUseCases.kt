package dev.muffar.moneyfikasi.domain.usecase.backup_restore

data class BackupRestoreUseCases(
    val backupData: BackupData,
    val restoreData: RestoreData,
    val deleteBackup: DeleteBackup,
    val deleteLatestBackup: DeleteLatestBackup,
    val takePersistableUriPermission: TakePersistableUriPermission,
    val scheduleAutoBackup: ScheduleAutoBackup,
    val cancelAutoBackup: CancelAutoBackup,
    val scheduleDriveAutoBackup: ScheduleDriveAutoBackup,
    val cancelDriveAutoBackup: CancelDriveAutoBackup
)
