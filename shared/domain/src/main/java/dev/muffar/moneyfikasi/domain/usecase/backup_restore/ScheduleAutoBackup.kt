package dev.muffar.moneyfikasi.domain.usecase.backup_restore

import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.repository.BackupRestoreRepository

class ScheduleAutoBackup(
    private val backupRestoreRepository: BackupRestoreRepository
) {

    operator fun invoke(isEnabled: Boolean, uri: String, period: TimePeriod) {
        backupRestoreRepository.scheduleAutoBackup(isEnabled, uri, period)
    }
}
