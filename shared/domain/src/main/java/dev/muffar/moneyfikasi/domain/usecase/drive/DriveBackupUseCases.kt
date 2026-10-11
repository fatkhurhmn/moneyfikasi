package dev.muffar.moneyfikasi.domain.usecase.drive

data class DriveBackupUseCases(
    val backupToDrive: BackupToDrive,
    val restoreFromDrive: RestoreFromDrive,
    val getDriveBackups: GetDriveBackups,
    val deleteDriveBackup: DeleteDriveBackup,
    val signInToDrive: SignInToDrive,
    val isDriveSignedIn: IsDriveSignedIn,
    val driveSignOut: DriveSignOut,
    val getDriveAccountEmail: GetDriveAccountEmail
)
