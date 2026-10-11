package dev.muffar.moneyfikasi.domain.usecase.backup_restore

import android.net.Uri
import dev.muffar.moneyfikasi.domain.repository.BackupRestoreRepository

class TakePersistableUriPermission(
    private val backupRestoreRepository: BackupRestoreRepository
) {

    operator fun invoke(uri: Uri) {
        backupRestoreRepository.takePersistableUriPermission(uri)
    }
}
