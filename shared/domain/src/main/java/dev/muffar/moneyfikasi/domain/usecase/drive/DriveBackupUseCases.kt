package dev.muffar.moneyfikasi.domain.usecase.drive

data class DriveBackupUseCases(
    val backupToDrive: BackupToDrive,
    val restoreFromDrive: RestoreFromDrive,
    val getDriveBackups: GetDriveBackups,
    val deleteDriveBackup: DeleteDriveBackup
)
