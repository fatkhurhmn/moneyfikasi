package dev.muffar.moneyfikasi.feature.applock.set_pin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.domain.usecase.preferences.PreferencesUseCases
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetPinViewModel @Inject constructor(
    private val preferencesUseCases: PreferencesUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(SetPinState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: SetPinEvent) {
        when (event) {
            is SetPinEvent.OnPinChanged -> onPinChanged(event.pin)
            is SetPinEvent.OnBackToEnterPin -> onBackToEnterPin()
            is SetPinEvent.OnCancel -> onCancel()
        }
    }

    private fun onPinChanged(pin: String) {
        _state.update { it.copy(currentPin = pin, error = ErrorMessage()) }
        if (pin.length == 4) {
            when (state.value.step) {
                SetPinStep.ENTER_PIN -> {
                    _state.update {
                        it.copy(
                            pin = pin,
                            currentPin = "",
                            step = SetPinStep.CONFIRM_PIN
                        )
                    }
                }

                SetPinStep.CONFIRM_PIN -> {
                    if (pin == state.value.pin) {
                        onSavePin(pin)
                    } else {
                        _state.update {
                            it.copy(
                                currentPin = "",
                                error = ErrorMessage("PINs do not match. Please try again.")
                            )
                        }
                    }
                }
            }
        }
    }

    private fun onBackToEnterPin() {
        _state.update {
            it.copy(
                step = SetPinStep.ENTER_PIN,
                pin = "",
                confirmPin = "",
                currentPin = "",
                error = ErrorMessage()
            )
        }
    }

    private fun onSavePin(pin: String) {
        viewModelScope.launch {
            preferencesUseCases.setAppLockPin(pin)
            preferencesUseCases.enableAppLock(true)
            _state.update { it.copy(isLoading = false) }
            _eventFlow.emit(UiEvent.SavePin)
        }
    }

    private fun onCancel() {
        viewModelScope.launch {
            _eventFlow.emit(UiEvent.NavigateBack)
        }
    }

    sealed class UiEvent {
        object SavePin : UiEvent()
        object NavigateBack : UiEvent()
    }
}
