package dev.muffar.moneyfikasi.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class NotificationSettings(
    val isAllowNotification: Boolean = true,
    val isRecurringTransactionNotificationEnabled: Boolean = true
)
