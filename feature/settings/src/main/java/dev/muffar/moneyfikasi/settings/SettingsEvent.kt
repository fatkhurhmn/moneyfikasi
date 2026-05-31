package dev.muffar.moneyfikasi.settings

import dev.muffar.moneyfikasi.domain.model.AppLanguage
import dev.muffar.moneyfikasi.domain.model.AppTheme
import dev.muffar.moneyfikasi.domain.model.AmountInputType

sealed class SettingsEvent {
    data class AppThemeChanged(val theme: AppTheme) : SettingsEvent()
    data class AppLanguageChanged(val language: AppLanguage) : SettingsEvent()
    data class AmountInputTypeChanged(val type: AmountInputType) : SettingsEvent()
}
