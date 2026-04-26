package dev.muffar.moneyfikasi.domain.usecase.preferences

import dev.muffar.moneyfikasi.domain.repository.PreferencesRepository

class SetAppLockPin(
    private val repository: PreferencesRepository
) {
    suspend operator fun invoke(pin: String) = repository.setAppLockPin(pin)
}
