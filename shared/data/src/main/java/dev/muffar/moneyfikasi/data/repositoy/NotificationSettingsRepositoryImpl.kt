package dev.muffar.moneyfikasi.data.repositoy

import dev.muffar.moneyfikasi.data.preferences.UiPreferences
import dev.muffar.moneyfikasi.domain.model.NotificationSettings
import dev.muffar.moneyfikasi.domain.repository.NotificationSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotificationSettingsRepositoryImpl @Inject constructor(
    private val uiPreferences: UiPreferences
) : NotificationSettingsRepository {
    override fun getNotificationSettings(): Flow<NotificationSettings> {
        return uiPreferences.uiSettings.map { it.notification }
    }

    override suspend fun setAllowNotification(isEnabled: Boolean) {
        uiPreferences.setAllowNotification(isEnabled)
    }

    override suspend fun setRecurringTransactionNotification(isEnabled: Boolean) {
        uiPreferences.setRecurringTransactionNotification(isEnabled)
    }
}
