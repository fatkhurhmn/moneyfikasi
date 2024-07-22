package dev.muffar.moneyfikasi.domain.usecase.backup_restore

import android.net.Uri
import dev.muffar.moneyfikasi.domain.repository.BackupRestoreRepository

class RestoreData(
    private val backupRestoreRepository: BackupRestoreRepository,
) {

    operator fun invoke(uri: Uri) {
        backupRestoreRepository.restoreData(uri)
    }
}