package dev.muffar.moneyfikasi.domain.usecase.preferences

import dev.muffar.moneyfikasi.domain.repository.PreferencesRepository

class SetBalanceVisibility(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(isVisible: Boolean) =
        preferencesRepository.setBalanceVisibility(isVisible)
}