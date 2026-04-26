package dev.muffar.moneyfikasi.domain.usecase.preferences

import dev.muffar.moneyfikasi.domain.model.AppLockType
import dev.muffar.moneyfikasi.domain.repository.PreferencesRepository

class SetAppLockType(
    private val repository: PreferencesRepository
) {
    suspend operator fun invoke(type: AppLockType) = repository.setAppLockType(type)
}
