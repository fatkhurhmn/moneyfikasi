package dev.muffar.moneyfikasi.domain.repository

interface BackupRestoreRepository {
    fun backupData(): Int
    fun restoreData()
}