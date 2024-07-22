package dev.muffar.moneyfikasi.backup_restore

import android.net.Uri

data class BackupRestoreState(
    val showBackupAlert: Boolean = false,
    val showRestoreAlert: Boolean = false,
    val selectedUri : Uri? = null
)