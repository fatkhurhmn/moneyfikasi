package dev.muffar.moneyfikasi.domain.usecase.notification

import dev.muffar.moneyfikasi.domain.model.NotificationSettings
import dev.muffar.moneyfikasi.domain.repository.NotificationSettingsRepository
import kotlinx.coroutines.flow.Flow

class GetNotificationSettings(
    private val notificationSettingsRepository: NotificationSettingsRepository
) {
    operator fun invoke(): Flow<NotificationSettings> {
        return notificationSettingsRepository.getNotificationSettings()
    }
}
