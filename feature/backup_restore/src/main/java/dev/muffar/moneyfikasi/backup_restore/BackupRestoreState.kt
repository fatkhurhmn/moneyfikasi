package dev.muffar.moneyfikasi.backup_restore

import dev.muffar.moneyfikasi.domain.model.AutoBackup
import dev.muffar.moneyfikasi.domain.model.DriveBackupFile
import dev.muffar.moneyfikasi.domain.model.LatestBackup

data class BackupRestoreState(
    val latestBackup: LatestBackup = LatestBackup(),
    val autoBackup: AutoBackup = AutoBackup(),
    val isDeletePreviousBackup: Boolean = true,
    val isLoading: Boolean = false,
    val isDriveSignedIn: Boolean = false,
    val driveAccountEmail: String = "",
    val driveBackup: DriveBackupFile? = null,
    val isDriveLoading: Boolean = false,
    val isDriveAutoBackupEnabled: Boolean = false,
    val driveAutoBackupPeriod: String = dev.muffar.moneyfikasi.domain.model.TimePeriod.DAILY.name,
)