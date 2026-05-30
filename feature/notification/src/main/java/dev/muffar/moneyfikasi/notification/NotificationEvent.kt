package dev.muffar.moneyfikasi.notification

sealed class NotificationEvent {
    data class AllowNotificationChanged(val isEnabled: Boolean) : NotificationEvent()
    data class RecurringTransactionNotificationChanged(val isEnabled: Boolean) : NotificationEvent()
}
