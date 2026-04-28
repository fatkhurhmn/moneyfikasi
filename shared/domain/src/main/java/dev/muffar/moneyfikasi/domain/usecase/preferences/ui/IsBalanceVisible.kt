package dev.muffar.moneyfikasi.domain.usecase.preferences.ui

import dev.muffar.moneyfikasi.domain.repository.UiPreferencesRepository

class IsBalanceVisible(
    private val uiPreferencesRepository: UiPreferencesRepository
) {
    operator fun invoke() = uiPreferencesRepository.isBalanceVisible()
}