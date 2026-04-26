package dev.muffar.moneyfikasi.domain.usecase.preferences

import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.repository.PreferencesRepository

class SetAutoBackupPeriod(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(period: TimePeriod) {
        preferencesRepository.setAutoBackupPeriod(period)
    }
}
