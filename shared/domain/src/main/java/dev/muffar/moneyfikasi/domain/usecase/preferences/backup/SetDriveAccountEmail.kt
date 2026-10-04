package dev.muffar.moneyfikasi.domain.usecase.preferences.backup

import dev.muffar.moneyfikasi.domain.repository.BackupSettingsRepository

class SetDriveAccountEmail(
    private val backupSettingsRepository: BackupSettingsRepository
) {
    suspend operator fun invoke(email: String) {
        backupSettingsRepository.setDriveAccountEmail(email)
    }
}
