package dev.muffar.moneyfikasi.data.preferences

import androidx.datastore.core.DataStore
import dev.muffar.moneyfikasi.domain.model.NotificationSettings
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationPreferences @Inject constructor(
    private val dataStore: DataStore<NotificationSettings>,
) {
    val notificationSettings: Flow<NotificationSettings> = dataStore.data

    suspend fun setAllowNotification(isEnabled: Boolean) {
        dataStore.updateData {
            it.copy(isAllowNotification = isEnabled)
        }
    }

    suspend fun setRecurringTransactionNotification(isEnabled: Boolean) {
        dataStore.updateData {
            it.copy(isRecurringTransactionNotificationEnabled = isEnabled)
        }
    }
}
