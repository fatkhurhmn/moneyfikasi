package dev.muffar.moneyfikasi.domain.usecase.preferences.ui

import dev.muffar.moneyfikasi.domain.model.AppTheme
import dev.muffar.moneyfikasi.domain.repository.UiSettingsRepository

class SetAppTheme(
    private val uiSettingsRepository: UiSettingsRepository
) {
    suspend operator fun invoke(theme: AppTheme) {
        uiSettingsRepository.setAppTheme(theme)
    }
}
