package dev.muffar.moneyfikasi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.UiSettings
import dev.muffar.moneyfikasi.domain.usecase.notification.NotificationUseCases
import dev.muffar.moneyfikasi.domain.usecase.preferences.security.SecuritySettingsUseCases
import dev.muffar.moneyfikasi.domain.usecase.preferences.ui.UiSettingsUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val securitySettingsUseCases: SecuritySettingsUseCases,
    private val uiSettingsUseCases: UiSettingsUseCases,
    private val notificationUseCases: NotificationUseCases,
) : ViewModel() {

    private val _isAppLockEnabled = MutableStateFlow<Boolean?>(null)
    val isAppLockEnabled = _isAppLockEnabled.asStateFlow()

    private val _uiSettings = MutableStateFlow(UiSettings())
    val uiSettings = _uiSettings.asStateFlow()

    init {
        checkAppLock()
        getUiSettings()
    }

    private fun checkAppLock() {
        viewModelScope.launch {
            val isEnabled = securitySettingsUseCases.getSecuritySettings()
                .first().isAppLockEnabled
            _isAppLockEnabled.update { isEnabled }
        }
    }

    private fun getUiSettings() {
        viewModelScope.launch {
            uiSettingsUseCases.getUiSettings().collect { uiSettings ->
                _uiSettings.update { uiSettings }
            }
        }
    }

    fun syncNotificationPermission(isEnabled: Boolean) {
        viewModelScope.launch {
            val notificationSettings = notificationUseCases.getNotificationSettings().first()
            if (notificationSettings.isAllowNotification != isEnabled) {
                notificationUseCases.setAllowNotification(isEnabled)
                if (!isEnabled) {
                    notificationUseCases.setRecurringTransactionNotification(false)
                }
            }
        }
    }
}
