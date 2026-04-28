package dev.muffar.moneyfikasi.domain.usecase.preferences.security

import dev.muffar.moneyfikasi.domain.repository.SecuritySettingsRepository

class IsBiometricEnabled(
    private val securitySettingsRepository: SecuritySettingsRepository
) {
    operator fun invoke() = securitySettingsRepository.isBiometricEnabled()
}