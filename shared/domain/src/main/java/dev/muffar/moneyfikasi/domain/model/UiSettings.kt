package dev.muffar.moneyfikasi.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UiSettings(
    val isBalanceVisible: Boolean = false,
    val isReportVisible: Boolean = false,
    val notification: NotificationSettings = NotificationSettings(),
    val appTheme: AppTheme = AppTheme.SYSTEM,
    val appLanguage: AppLanguage = AppLanguage.SYSTEM
)
