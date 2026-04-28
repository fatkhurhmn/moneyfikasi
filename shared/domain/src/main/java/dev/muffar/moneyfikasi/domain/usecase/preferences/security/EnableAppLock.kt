package dev.muffar.moneyfikasi.domain.usecase.preferences.security

import dev.muffar.moneyfikasi.domain.repository.SecuritySettingsRepository

class EnableAppLock(
    private val securitySettingsRepository: SecuritySettingsRepository
) {
    suspend operator fun invoke(enable: Boolean) = securitySettingsRepository.enableAppLock(enable)
}