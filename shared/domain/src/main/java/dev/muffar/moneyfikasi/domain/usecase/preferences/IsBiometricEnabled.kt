package dev.muffar.moneyfikasi.domain.usecase.preferences

import dev.muffar.moneyfikasi.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow

class IsBiometricEnabled(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return preferencesRepository.isBiometricEnabled()
    }
}
