package dev.muffar.moneyfikasi.data.repositoy

import dev.muffar.moneyfikasi.data.preferences.NotificationPreferences
import dev.muffar.moneyfikasi.domain.model.NotificationSettings
import dev.muffar.moneyfikasi.domain.repository.NotificationSettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationSettingsRepositoryImpl @Inject constructor(
    private val notificationPreferences: NotificationPreferences
) : NotificationSettingsRepository {
    override fun getNotificationSettings(): Flow<NotificationSettings> {
        return notificationPreferences.notificationSettings
    }

    override suspend fun setAllowNotification(isEnabled: Boolean) {
        notificationPreferences.setAllowNotification(isEnabled)
    }

    override suspend fun setRecurringTransactionNotification(isEnabled: Boolean) {
        notificationPreferences.setRecurringTransactionNotification(isEnabled)
    }
}
