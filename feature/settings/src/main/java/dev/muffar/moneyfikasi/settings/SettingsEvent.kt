package dev.muffar.moneyfikasi.settings

import dev.muffar.moneyfikasi.domain.model.AppTheme

sealed class SettingsEvent {
    data class OnAppThemeChanged(val theme: AppTheme) : SettingsEvent()
}
