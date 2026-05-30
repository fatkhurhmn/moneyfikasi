package dev.muffar.moneyfikasi.domain.usecase.preferences.ui

import dev.muffar.moneyfikasi.domain.repository.UiSettingsRepository

class SetAllowNotification(
    private val uiSettingsRepository: UiSettingsRepository
) {
    suspend operator fun invoke(isEnabled: Boolean) =
        uiSettingsRepository.setAllowNotification(isEnabled)
}
