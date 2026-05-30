package dev.muffar.moneyfikasi.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.AppLanguage
import dev.muffar.moneyfikasi.domain.model.AppTheme
import dev.muffar.moneyfikasi.domain.usecase.preferences.ui.UiSettingsUseCases
import dev.muffar.moneyfikasi.notification.NotificationEvent
import dev.muffar.moneyfikasi.notification.NotificationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val uiSettingsUseCases: UiSettingsUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            uiSettingsUseCases.getUiSettings().collectLatest { uiSettings ->
                _state.update {
                    it.copy(
                        appTheme = uiSettings.appTheme,
                        appLanguage = uiSettings.appLanguage,
                        notification = NotificationState(
                            isAllowNotification = uiSettings.isAllowNotification,
                            isRecurringTransactionNotificationEnabled =
                            uiSettings.isRecurringTransactionNotificationEnabled
                        )
                    )
                }
            }
        }
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.AppThemeChanged -> onAppThemeChange(event.theme)
            is SettingsEvent.AppLanguageChanged -> onAppLanguageChange(event.language)
            is SettingsEvent.Notification -> onNotificationEvent(event.event)
        }
    }

    private fun onNotificationEvent(event: NotificationEvent) {
        when (event) {
            is NotificationEvent.AllowNotificationChanged -> onAllowNotificationChange(event.isEnabled)
            is NotificationEvent.RecurringTransactionNotificationChanged ->
                onRecurringTransactionNotificationChange(event.isEnabled)
        }
    }

    private fun onAppThemeChange(theme: AppTheme) {
        viewModelScope.launch {
            uiSettingsUseCases.setAppTheme(theme)
        }
    }

    private fun onAppLanguageChange(language: AppLanguage) {
        viewModelScope.launch {
            uiSettingsUseCases.setAppLanguage(language)
        }
    }

    private fun onAllowNotificationChange(isEnabled: Boolean) {
        viewModelScope.launch {
            uiSettingsUseCases.setAllowNotification(isEnabled)
            if (!isEnabled) {
                uiSettingsUseCases.setRecurringTransactionNotification(false)
            }
        }
    }

    private fun onRecurringTransactionNotificationChange(isEnabled: Boolean) {
        viewModelScope.launch {
            uiSettingsUseCases.setRecurringTransactionNotification(isEnabled)
        }
    }
}
