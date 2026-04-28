package dev.muffar.moneyfikasi.domain.usecase.preferences.security

import dev.muffar.moneyfikasi.domain.repository.SecurityPreferencesRepository

class SetAppLockPin(
    private val securityPreferencesRepository: SecurityPreferencesRepository
) {
    suspend operator fun invoke(pin: String) = securityPreferencesRepository.setAppLockPin(pin)
}