package dev.muffar.moneyfikasi.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.usecase.preferences.ui.UiSettingsUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val uiSettingsUseCases: UiSettingsUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(NotificationState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            uiSettingsUseCases.getUiSettings().collectLatest { uiSettings ->
                _state.update {
                    it.copy(
                        isAllowNotification = uiSettings.isAllowNotification,
                        isRecurringTransactionNotificationEnabled =
                        uiSettings.isRecurringTransactionNotificationEnabled
                    )
                }
            }
        }
    }

    fun onEvent(event: NotificationEvent) {
        when (event) {
            is NotificationEvent.AllowNotificationChanged -> onAllowNotificationChange(event.isEnabled)
            is NotificationEvent.RecurringTransactionNotificationChanged ->
                onRecurringTransactionNotificationChange(event.isEnabled)
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
