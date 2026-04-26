package dev.muffar.moneyfikasi.domain.usecase.preferences

import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow

class GetAutoBackupPeriod(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke(): Flow<TimePeriod> {
        return preferencesRepository.getAutoBackupPeriod()
    }
}
