package dev.muffar.moneyfikasi.domain.usecase.preferences

import dev.muffar.moneyfikasi.domain.repository.PreferencesRepository

class GetAppLockType(
    private val repository: PreferencesRepository
) {
    operator fun invoke() = repository.getAppLockType()
}
