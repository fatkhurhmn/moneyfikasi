package dev.muffar.moneyfikasi.feature.applock.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.EnterPinType
import dev.muffar.moneyfikasi.domain.usecase.preferences.PreferencesUseCases
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppLockViewModel @Inject constructor(
    private val preferencesUseCases: PreferencesUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(AppLockState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        loadAppLockSettings()
    }

    fun onEvent(event: AppLockEvent) {
        when (event) {
            is AppLockEvent.OnAppLockEnabledChanged -> onAppLockEnabledChanged(event.isEnabled)
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
        viewModelScope.launch {
            if (isEnabled && state.value.pin.isEmpty()) {
                _eventFlow.emit(UiEvent.NavigateToEnterPin(EnterPinType.SET_PIN))
                return@launch
            }

            preferencesUseCases.enableAppLock(isEnabled)
            if (!isEnabled) preferencesUseCases.setAppLockPin("")
        }
    }

    sealed class UiEvent {
        data class NavigateToEnterPin(val type: EnterPinType) : UiEvent()
    }
}
