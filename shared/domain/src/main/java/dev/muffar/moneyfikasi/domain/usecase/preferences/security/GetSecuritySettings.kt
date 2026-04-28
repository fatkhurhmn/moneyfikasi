package dev.muffar.moneyfikasi.domain.usecase.preferences.security

import dev.muffar.moneyfikasi.domain.repository.SecuritySettingsRepository

class GetSecuritySettings(
    private val securitySettingsRepository: SecuritySettingsRepository
) {
    operator fun invoke() = securitySettingsRepository.getSecuritySettings()
}
