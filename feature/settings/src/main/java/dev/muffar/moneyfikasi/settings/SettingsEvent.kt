package dev.muffar.moneyfikasi.settings

import dev.muffar.moneyfikasi.domain.model.AppLanguage
import dev.muffar.moneyfikasi.domain.model.AppTheme

sealed class SettingsEvent {
    data class AppThemeChanged(val theme: AppTheme) : SettingsEvent()
    data class AppLanguageChanged(val language: AppLanguage) : SettingsEvent()
    data class AllowNotificationChanged(val isEnabled: Boolean) : SettingsEvent()
    data class RecurringTransactionNotificationChanged(val isEnabled: Boolean) : SettingsEvent()
}
