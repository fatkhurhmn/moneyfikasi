package dev.muffar.moneyfikasi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.EnterPinType
import dev.muffar.moneyfikasi.domain.model.UiSettings
import dev.muffar.moneyfikasi.domain.usecase.notification.NotificationUseCases
import dev.muffar.moneyfikasi.domain.usecase.preferences.security.SecuritySettingsUseCases
import dev.muffar.moneyfikasi.domain.usecase.preferences.ui.UiSettingsUseCases
import dev.muffar.moneyfikasi.navigation.Screen
import kotlinx.coroutines.delay
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

    private val _postSplashRoute = MutableStateFlow<String?>(null)
    val postSplashRoute = _postSplashRoute.asStateFlow()

    init {
        preparePostSplashRoute()
        getUiSettings()
    }

    private fun preparePostSplashRoute() {
        viewModelScope.launch {
            val isEnabled = securitySettingsUseCases.getSecuritySettings()
                .first().isAppLockEnabled
            _isAppLockEnabled.update { isEnabled }
            delay(SPLASH_DURATION_MILLIS)
            _postSplashRoute.update {
                if (isEnabled) {
                    Screen.EnterPin.routeWithArg(EnterPinType.ENTER_PIN)
                } else {
                    Screen.Home.route
                }
            }
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
                notificationUseCases.setRecurringTransactionNotification(isEnabled)
            }
        }
    }

    private companion object {
        const val SPLASH_DURATION_MILLIS = 2_000L
    }
}
