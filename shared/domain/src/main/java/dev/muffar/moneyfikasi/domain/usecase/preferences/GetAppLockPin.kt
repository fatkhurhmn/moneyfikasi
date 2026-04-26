package dev.muffar.moneyfikasi.domain.usecase.preferences

import dev.muffar.moneyfikasi.domain.repository.PreferencesRepository

class GetAppLockPin(
    private val repository: PreferencesRepository
) {
    operator fun invoke() = repository.getAppLockPin()
}
