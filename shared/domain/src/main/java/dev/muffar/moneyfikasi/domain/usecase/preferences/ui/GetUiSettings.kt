package dev.muffar.moneyfikasi.domain.usecase.preferences.ui

import dev.muffar.moneyfikasi.domain.repository.UiSettingsRepository

class GetUiSettings(
    private val uiSettingsRepository: UiSettingsRepository
) {
    operator fun invoke() = uiSettingsRepository.getUiSettings()
}
