package dev.muffar.moneyfikasi.domain.usecase.preferences.security

import dev.muffar.moneyfikasi.domain.repository.SecuritySettingsRepository

class GetAppLockPin(
    private val securitySettingsRepository: SecuritySettingsRepository
) {
    operator fun invoke() = securitySettingsRepository.getAppLockPin()
}