package dev.muffar.moneyfikasi.domain.usecase.preferences

import dev.muffar.moneyfikasi.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow

class IsDeletePreviousBackup(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return preferencesRepository.isDeletePreviousBackup()
    }
}
