package dev.muffar.moneyfikasi.domain.usecase.preferences

import dev.muffar.moneyfikasi.domain.repository.PreferencesRepository

class IsAppLockEnabled(
    private val repository: PreferencesRepository
) {
    operator fun invoke() = repository.isAppLockEnabled()
}
