package dev.muffar.moneyfikasi.applock.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.domain.usecase.preferences.PreferencesUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AppLockViewModel @Inject constructor(
    private val application: Application,
    private val preferencesUseCases: PreferencesUseCases,
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(AppLockState())
    val state = _state.asStateFlow()

    init {
        loadAppLockSettings()
    }

    fun onEvent(event: AppLockEvent) {
        when (event) {
            is AppLockEvent.OnAppLockEnabledChanged -> onAppLockEnabledChanged(event.isEnabled)
            is AppLockEvent.OnPinChanged -> onPinChanged(event.pin)
            is AppLockEvent.OnConfirmPinChanged -> onConfirmPinChanged(event.pin)
            is AppLockEvent.OnSaveAppLock -> onSaveAppLock()
        }
    }

    private fun loadAppLockSettings() {
        combine(
            preferencesUseCases.isAppLockEnabled(),
            preferencesUseCases.getAppLockPin()
        ) { isAppLockEnable, pin ->
            _state.update {
                it.copy(
                    isAppLockEnabled = isAppLockEnable,
                    pin = pin,
                    confirmPin = pin
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun onAppLockEnabledChanged(isEnabled: Boolean) {
        _state.update {
            it.copy(
                isAppLockEnabled = isEnabled,
            )
        }
    }

    private fun onPinChanged(pin: String) {
        _state.update { it.copy(pin = pin, error = ErrorMessage()) }
    }

    private fun onConfirmPinChanged(pin: String) {
        _state.update { it.copy(confirmPin = pin, error = ErrorMessage()) }
    }

    private fun onSaveAppLock() {
    }
}
