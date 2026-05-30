package dev.muffar.moneyfikasi.domain.usecase.notification

data class NotificationUseCases(
    val getNotificationSettings: GetNotificationSettings,
    val setAllowNotification: SetAllowNotification,
    val setRecurringTransactionNotification: SetRecurringTransactionNotification
)
