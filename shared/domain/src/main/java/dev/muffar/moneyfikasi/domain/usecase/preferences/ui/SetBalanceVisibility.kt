package dev.muffar.moneyfikasi.domain.usecase.preferences.ui

import dev.muffar.moneyfikasi.domain.repository.UiPreferencesRepository

class SetBalanceVisibility(
    private val uiPreferencesRepository: UiPreferencesRepository
) {
    suspend operator fun invoke(isVisible: Boolean) = uiPreferencesRepository.setBalanceVisibility(isVisible)
}