package dev.muffar.moneyfikasi.domain.usecase.preferences.ui

import dev.muffar.moneyfikasi.domain.repository.UiSettingsRepository

class SetBudgetCutoffDay(
    private val uiSettingsRepository: UiSettingsRepository
) {
    suspend operator fun invoke(day: Int) {
        uiSettingsRepository.setBudgetCutoffDay(day)
    }
}
