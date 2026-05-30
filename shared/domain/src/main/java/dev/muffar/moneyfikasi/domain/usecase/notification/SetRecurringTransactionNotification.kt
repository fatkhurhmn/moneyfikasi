package dev.muffar.moneyfikasi.domain.usecase.notification

import dev.muffar.moneyfikasi.domain.repository.NotificationSettingsRepository

class SetRecurringTransactionNotification(
    private val notificationSettingsRepository: NotificationSettingsRepository
) {
    suspend operator fun invoke(isEnabled: Boolean) =
        notificationSettingsRepository.setRecurringTransactionNotification(isEnabled)
}
