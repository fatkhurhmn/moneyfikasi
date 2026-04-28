package dev.muffar.moneyfikasi.domain.usecase.preferences.security

import dev.muffar.moneyfikasi.domain.repository.SecurityPreferencesRepository

class EnableBiometric(
    private val securityPreferencesRepository: SecurityPreferencesRepository
) {
    suspend operator fun invoke(enable: Boolean) = securityPreferencesRepository.enableBiometric(enable)
}