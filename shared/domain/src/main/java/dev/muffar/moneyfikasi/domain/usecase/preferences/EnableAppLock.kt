package dev.muffar.moneyfikasi.domain.usecase.preferences

import dev.muffar.moneyfikasi.domain.repository.PreferencesRepository

class EnableAppLock(
    private val repository: PreferencesRepository
) {
    suspend operator fun invoke(enable: Boolean) = repository.enableAppLock(enable)
}
