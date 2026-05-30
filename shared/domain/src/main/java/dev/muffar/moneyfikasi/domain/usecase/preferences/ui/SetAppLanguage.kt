package dev.muffar.moneyfikasi.domain.usecase.preferences.ui

import dev.muffar.moneyfikasi.domain.model.AppLanguage
import dev.muffar.moneyfikasi.domain.repository.UiSettingsRepository

class SetAppLanguage(
    private val uiSettingsRepository: UiSettingsRepository
) {
    suspend operator fun invoke(language: AppLanguage) {
        uiSettingsRepository.setAppLanguage(language)
    }
}
