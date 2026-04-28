package dev.muffar.moneyfikasi.domain.usecase.preferences.security

import dev.muffar.moneyfikasi.domain.repository.SecuritySettingsRepository

class EnableBiometric(
    private val securitySettingsRepository: SecuritySettingsRepository
) {
    suspend operator fun invoke(enable: Boolean) = securitySettingsRepository.enableBiometric(enable)
}