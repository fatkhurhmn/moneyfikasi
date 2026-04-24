package dev.muffar.moneyfikasi.domain.usecase.preferences

import dev.muffar.moneyfikasi.domain.repository.PreferencesRepository

class SetAutoBackupPeriod(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(period: String) {
        preferencesRepository.setAutoBackupPeriod(period)
    }
}