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
    val driveBackups: List<DriveBackupFile> = emptyList(),
    val isDriveLoading: Boolean = false,
)