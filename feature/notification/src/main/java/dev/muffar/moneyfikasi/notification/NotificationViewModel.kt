package dev.muffar.moneyfikasi.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.usecase.notification.NotificationUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationUseCases: NotificationUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(NotificationState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            notificationUseCases.getNotificationSettings().collectLatest { notificationSettings ->
                _state.update {
                    it.copy(
                        isAllowNotification = notificationSettings.isAllowNotification,
                        isRecurringTransactionNotificationEnabled =
                        notificationSettings.isRecurringTransactionNotificationEnabled
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
            notificationUseCases.setAllowNotification(isEnabled)
            if (!isEnabled) {
                notificationUseCases.setRecurringTransactionNotification(false)
            }
        }
    }

    private fun onRecurringTransactionNotificationChange(isEnabled: Boolean) {
        viewModelScope.launch {
            notificationUseCases.setRecurringTransactionNotification(isEnabled)
        }
    }
}
