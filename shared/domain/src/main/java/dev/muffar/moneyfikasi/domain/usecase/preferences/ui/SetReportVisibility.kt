package dev.muffar.moneyfikasi.domain.usecase.preferences.ui

import dev.muffar.moneyfikasi.domain.repository.UiSettingsRepository

class SetReportVisibility(
    private val uiSettingsRepository: UiSettingsRepository
) {
    suspend operator fun invoke(isVisible: Boolean) = uiSettingsRepository.setReportVisibility(isVisible)
}