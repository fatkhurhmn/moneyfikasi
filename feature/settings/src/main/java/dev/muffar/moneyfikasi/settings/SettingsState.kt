package dev.muffar.moneyfikasi.settings

import dev.muffar.moneyfikasi.domain.model.AppTheme

data class SettingsState(
    val appTheme: AppTheme = AppTheme.SYSTEM
)
