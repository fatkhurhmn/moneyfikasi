package dev.muffar.moneyfikasi.feature.applock.enter_pin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.usecase.preferences.PreferencesUseCases
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import dev.muffar.moneyfikasi.domain.model.ErrorMessage

@HiltViewModel
class EnterPinViewModel @Inject constructor(
    private val preferencesUseCases: PreferencesUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(EnterPinState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: EnterPinEvent) {
        when (event) {
            is EnterPinEvent.OnPinChanged -> onPinChanged(event.pin)
            is EnterPinEvent.OnBackToEnterPin -> onBackToEnterPin()
            is EnterPinEvent.OnCancel -> onCancel()
        }
    }

    private fun onPinChanged(pin: String) {
        _state.update { it.copy(currentPin = pin, error = ErrorMessage()) }
        if (pin.length == 4) {
            when (state.value.step) {
                EnterPinStep.ENTER_PIN -> {
                    _state.update {
                        it.copy(
                            pin = pin,
                            currentPin = "",
                            step = EnterPinStep.CONFIRM_PIN
                        )
                    }
                }

                EnterPinStep.CONFIRM_PIN -> {
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
                step = EnterPinStep.ENTER_PIN,
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
