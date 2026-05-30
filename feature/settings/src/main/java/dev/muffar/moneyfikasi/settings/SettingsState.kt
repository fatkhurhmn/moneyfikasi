package dev.muffar.moneyfikasi.settings

import dev.muffar.moneyfikasi.domain.model.AppLanguage
import dev.muffar.moneyfikasi.domain.model.AppTheme
import dev.muffar.moneyfikasi.notification.NotificationState

data class SettingsState(
    val appTheme: AppTheme = AppTheme.SYSTEM,
    val appLanguage: AppLanguage = AppLanguage.SYSTEM,
    val notification: NotificationState = NotificationState()
)
