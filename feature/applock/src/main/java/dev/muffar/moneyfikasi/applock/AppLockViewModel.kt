package dev.muffar.moneyfikasi.applock

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.muffar.moneyfikasi.domain.model.AppLockType
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.domain.usecase.preferences.PreferencesUseCases
import dev.muffar.moneyfikasi.resource.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppLockViewModel @Inject constructor(
    private val preferencesUseCases: PreferencesUseCases,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _state = MutableStateFlow(AppLockState())
    val state = _state.asStateFlow()

    init {
        checkBiometricAvailability()
        loadAppLockSettings()
    }

    fun onEvent(event: AppLockEvent) {
        when (event) {
            is AppLockEvent.OnAppLockEnabledChanged -> onAppLockEnabledChanged(event.isEnabled)
            is AppLockEvent.OnAppLockTypeChanged -> onAppLockTypeChanged(event.type)
            is AppLockEvent.OnPinChanged -> onPinChanged(event.pin)
            is AppLockEvent.OnConfirmPinChanged -> onConfirmPinChanged(event.pin)
            is AppLockEvent.OnSaveAppLock -> onSaveAppLock()
        }
    }

    private fun loadAppLockSettings() {
        combine(
            preferencesUseCases.getAppLockType(),
            preferencesUseCases.getAppLockPin()
        ) { type, pin ->
            _state.update {
                it.copy(
                    appLockType = type,
                    isAppLockEnabled = type != AppLockType.NONE,
                    pin = pin,
                    confirmPin = pin
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun checkBiometricAvailability() {
        val biometricManager = BiometricManager.from(context)
        val isAvailable = when (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> false
        }
        _state.update { it.copy(isBiometricAvailable = isAvailable) }
    }

    private fun onAppLockEnabledChanged(isEnabled: Boolean) {
        _state.update { it.copy(isAppLockEnabled = isEnabled) }
        if (!isEnabled) {
            viewModelScope.launch {
                preferencesUseCases.setAppLockType(AppLockType.NONE)
                preferencesUseCases.setAppLockPin("")
            }
        }
    }

    private fun onAppLockTypeChanged(type: AppLockType) {
        _state.update { it.copy(appLockType = type) }
    }

    private fun onPinChanged(pin: String) {
        _state.update { it.copy(pin = pin, error = ErrorMessage()) }
    }

    private fun onConfirmPinChanged(pin: String) {
        _state.update { it.copy(confirmPin = pin, error = ErrorMessage()) }
    }

    private fun onSaveAppLock() {
        if (state.value.appLockType == AppLockType.PIN) {
            if (state.value.pin.length < 4) {
                _state.update { it.copy(error = ErrorMessage(context.getString(R.string.pin_min_length))) }
                return
            }
            if (state.value.pin != state.value.confirmPin) {
                _state.update { it.copy(error = ErrorMessage(context.getString(R.string.pin_not_match))) }
                return
            }
        }

        viewModelScope.launch {
            _state.update { it.copy(error = ErrorMessage()) }
            preferencesUseCases.setAppLockType(state.value.appLockType)
            if (state.value.appLockType == AppLockType.PIN) {
                preferencesUseCases.setAppLockPin(state.value.pin)
            } else {
                preferencesUseCases.setAppLockPin("")
            }
        }
    }
}
