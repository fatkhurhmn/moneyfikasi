package dev.muffar.moneyfikasi.domain.usecase.preferences.ui

import dev.muffar.moneyfikasi.domain.repository.UiSettingsRepository

class SetRecurringTransactionNotification(
    private val uiSettingsRepository: UiSettingsRepository
) {
    suspend operator fun invoke(isEnabled: Boolean) =
        uiSettingsRepository.setRecurringTransactionNotification(isEnabled)
}
