package dev.muffar.moneyfikasi.domain.usecase.backup_restore

import android.net.Uri
import dev.muffar.moneyfikasi.domain.repository.BackupRestoreRepository

class BackupData(
    private val backupRestoreRepository: BackupRestoreRepository
) {

    suspend operator fun invoke(uri: Uri): Result<String> {
        return backupRestoreRepository.backupData(uri)
    }
}