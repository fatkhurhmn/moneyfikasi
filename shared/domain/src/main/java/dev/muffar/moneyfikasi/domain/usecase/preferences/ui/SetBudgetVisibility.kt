package dev.muffar.moneyfikasi.domain.usecase.preferences.ui

import dev.muffar.moneyfikasi.domain.repository.UiSettingsRepository

class SetBudgetVisibility(
    private val uiSettingsRepository: UiSettingsRepository
) {
    suspend operator fun invoke(isVisible: Boolean) =
        uiSettingsRepository.setBudgetVisibility(isVisible)
}
