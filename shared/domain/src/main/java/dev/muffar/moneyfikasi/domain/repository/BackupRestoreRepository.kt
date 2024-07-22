package dev.muffar.moneyfikasi.domain.repository

import android.net.Uri

interface BackupRestoreRepository {
    fun backupData(uri: Uri): Int
    fun restoreData(uri: Uri, restart: Boolean = true)
}