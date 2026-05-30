package dev.muffar.moneyfikasi.notification

data class NotificationState(
    val isAllowNotification: Boolean = true,
    val isRecurringTransactionNotificationEnabled: Boolean = true
)
