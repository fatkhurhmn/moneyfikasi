package dev.muffar.moneyfikasi.domain.repository

import dev.muffar.moneyfikasi.domain.model.NotificationSettings
import kotlinx.coroutines.flow.Flow

interface NotificationSettingsRepository {
    fun getNotificationSettings(): Flow<NotificationSettings>
    suspend fun setAllowNotification(isEnabled: Boolean)
    suspend fun setRecurringTransactionNotification(isEnabled: Boolean)
}
