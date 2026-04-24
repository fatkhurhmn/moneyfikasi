package dev.muffar.moneyfikasi.domain.usecase.preferences

import dev.muffar.moneyfikasi.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow

class GetAutoBackupUri(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke(): Flow<String> {
        return preferencesRepository.getAutoBackupUri()
    }
}