package dev.muffar.moneyfikasi.domain.usecase.preferences.security

import dev.muffar.moneyfikasi.domain.repository.SecuritySettingsRepository

class SetAppLockPin(
    private val securitySettingsRepository: SecuritySettingsRepository
) {
    suspend operator fun invoke(pin: String) = securitySettingsRepository.setAppLockPin(pin)
}