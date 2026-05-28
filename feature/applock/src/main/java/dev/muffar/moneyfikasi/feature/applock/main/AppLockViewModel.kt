package dev.muffar.moneyfikasi.feature.applock.main

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.muffar.moneyfikasi.domain.model.EnterPinType
import dev.muffar.moneyfikasi.domain.usecase.preferences.security.SecuritySettingsUseCases
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppLockViewModel @Inject constructor(
    private val securitySettingsUseCases: SecuritySettingsUseCases,
    @param:ApplicationContext private val context: Context,
) : ViewModel() {

    private val _state = MutableStateFlow(AppLockState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        checkBiometricSupport()
        loadAppLockSettings()
    }

    fun onEvent(event: AppLockEvent) {
        when (event) {
            is AppLockEvent.OnAppLockEnabledChanged -> onAppLockEnabledChanged(event.isEnabled)
            is AppLockEvent.OnBiometricEnabledChanged -> onBiometricEnabledChanged(event.isEnabled)
        }
    }

    private fun checkBiometricSupport() {
        val biometricManager = BiometricManager.from(context)
        val isSupported =
            when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
                BiometricManager.BIOMETRIC_SUCCESS -> true
                else -> false
            }
        _state.update { it.copy(isBiometricSupported = isSupported) }
    }

    private fun loadAppLockSettings() {
        securitySettingsUseCases.getSecuritySettings().onEach { settings ->
            _state.update {
                it.copy(
                    isAppLockEnabled = settings.isAppLockEnabled,
                    isBiometricEnabled = settings.isBiometricEnabled,
                    pin = settings.appLockPin,
                    confirmPin = settings.appLockPin
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun onAppLockEnabledChanged(isEnabled: Boolean) {
        viewModelScope.launch {
            if (isEnabled && state.value.pin.isEmpty()) {
                _eventFlow.emit(UiEvent.NavigateToEnterPin(EnterPinType.SET_PIN))
                return@launch
            }

            if (!isEnabled && state.value.pin.isNotEmpty()) {
                _eventFlow.emit(UiEvent.NavigateToEnterPin(EnterPinType.DISABLE_PIN))
                return@launch
            }

            securitySettingsUseCases.enableAppLock(isEnabled)
            if (!isEnabled) {
                securitySettingsUseCases.setAppLockPin("")
                securitySettingsUseCases.enableBiometric(false)
            }
        }
    }

    private fun onBiometricEnabledChanged(isEnabled: Boolean) {
        viewModelScope.launch {
            securitySettingsUseCases.enableBiometric(isEnabled)
        }
    }

    sealed class UiEvent {
        data class NavigateToEnterPin(val type: EnterPinType) : UiEvent()
    }
}
