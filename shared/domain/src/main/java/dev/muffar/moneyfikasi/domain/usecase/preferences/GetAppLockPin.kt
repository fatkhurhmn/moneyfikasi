package dev.muffar.moneyfikasi.domain.usecase.preferences

import dev.muffar.moneyfikasi.domain.repository.SecurityPreferencesRepository

class GetAppLockPin(
    private val securityPreferencesRepository: SecurityPreferencesRepository
) {
    operator fun invoke() = securityPreferencesRepository.getAppLockPin()
}