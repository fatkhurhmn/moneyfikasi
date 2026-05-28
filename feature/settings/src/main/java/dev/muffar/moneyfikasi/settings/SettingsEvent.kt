package dev.muffar.moneyfikasi.settings

import dev.muffar.moneyfikasi.domain.model.AppTheme

sealed class SettingsEvent {
    data class AppThemeChanged(val theme: AppTheme) : SettingsEvent()
}
