package dev.muffar.moneyfikasi.domain.usecase.preferences.ui

import dev.muffar.moneyfikasi.domain.repository.UiSettingsRepository

class SetTransactionCalendarMode(
    private val uiSettingsRepository: UiSettingsRepository
) {
    suspend operator fun invoke(isCalendarMode: Boolean) {
        uiSettingsRepository.setTransactionCalendarMode(isCalendarMode)
    }
}
