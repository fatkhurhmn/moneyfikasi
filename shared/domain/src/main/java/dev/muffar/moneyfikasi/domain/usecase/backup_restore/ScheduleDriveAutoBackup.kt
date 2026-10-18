package dev.muffar.moneyfikasi.domain.usecase.backup_restore

import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.repository.BackupRestoreRepository

class ScheduleDriveAutoBackup(
    private val backupRestoreRepository: BackupRestoreRepository
) {

    operator fun invoke(isSignedIn: Boolean, period: TimePeriod) {
        backupRestoreRepository.scheduleDriveAutoBackup(isSignedIn, period)
    }
}
