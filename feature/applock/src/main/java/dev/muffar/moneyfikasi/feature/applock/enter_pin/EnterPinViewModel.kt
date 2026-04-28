package dev.muffar.moneyfikasi.feature.applock.enter_pin

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.EnterPinStep
import dev.muffar.moneyfikasi.domain.model.EnterPinType
import dev.muffar.moneyfikasi.domain.usecase.preferences.PreferencesUseCases
import dev.muffar.moneyfikasi.navigation.Screen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EnterPinViewModel @Inject constructor(
    private val preferencesUseCases: PreferencesUseCases,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(EnterPinState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        val typeStr = savedStateHandle.get<String>(Screen.EnterPin.TYPE)
        val type = EnterPinType.valueOf(typeStr ?: EnterPinType.ENTER_PIN.name)
        val step = when (type) {
            EnterPinType.SET_PIN -> EnterPinStep.ENTER_PIN
            EnterPinType.ENTER_PIN -> EnterPinStep.ENTER_PIN
            EnterPinType.RESET_PIN -> EnterPinStep.VERIFY_CURRENT_PIN
            EnterPinType.DISABLE_PIN -> EnterPinStep.ENTER_PIN
        }
        _state.update { it.copy(type = type, step = step) }

        viewModelScope.launch {
            val savedPin = preferencesUseCases.getAppLockPin().first()
            _state.update { it.copy(savedPin = savedPin) }
        }
    }

    fun onEvent(event: EnterPinEvent) {
        when (event) {
            is EnterPinEvent.OnPinChanged -> onPinChanged(event.pin)
        }
    }

    private fun onPinChanged(input: String) {
        _state.update { it.copy(currentInput = input, errorMessage = "") }
        if (input.length == 4) {
            handlePinComplete(input)
        }
    }

    private fun handlePinComplete(input: String) {
        when (state.value.type) {
            EnterPinType.SET_PIN -> handleSetPin(input)
            EnterPinType.ENTER_PIN -> handleEnterPin(input)
            EnterPinType.RESET_PIN -> handleResetPin(input)
            EnterPinType.DISABLE_PIN -> handleDisablePin(input)
        }
    }

    private fun handleSetPin(input: String) {
        when (state.value.step) {
            EnterPinStep.ENTER_PIN -> {
                _state.update {
                    it.copy(
                        newPin = input,
                        currentInput = "",
                        step = EnterPinStep.CONFIRM_PIN,
                    )
                }
            }

            EnterPinStep.CONFIRM_PIN -> {
                if (input == state.value.newPin) {
                    savePin(input)
                } else {
                    onInputPinError(
                        step = EnterPinStep.ENTER_PIN,
                        message = "PINs do not match. Please try again."
                    )
                }
            }

            else -> {}
        }
    }

    private fun handleEnterPin(input: String) {
        if (input == state.value.savedPin) {
            viewModelScope.launch {
                _eventFlow.emit(UiEvent.SavePin)
            }
        } else {
            onInputPinError(
                step = EnterPinStep.ENTER_PIN,
                message = "Incorrect PIN. Please try again."
            )
        }
    }

    private fun handleResetPin(input: String) {
        when (state.value.step) {
            EnterPinStep.VERIFY_CURRENT_PIN -> {
                if (input == state.value.savedPin) {
                    _state.update {
                        it.copy(
                            currentInput = "",
                            step = EnterPinStep.ENTER_NEW_PIN
                        )
                    }
                } else {
                    onInputPinError(
                        step = EnterPinStep.VERIFY_CURRENT_PIN,
                        message = "Incorrect current PIN. Please try again."
                    )
                }
            }

            EnterPinStep.ENTER_NEW_PIN -> {
                _state.update {
                    it.copy(
                        newPin = input,
                        currentInput = "",
                        step = EnterPinStep.CONFIRM_NEW_PIN
                    )
                }
            }

            EnterPinStep.CONFIRM_NEW_PIN -> {
                if (input == state.value.newPin) {
                    savePin(input)
                } else {
                    onInputPinError(
                        step = EnterPinStep.ENTER_NEW_PIN,
                        message = "PINs do not match. Please try again."
                    )
                }
            }

            else -> {}
        }
    }

    private fun handleDisablePin(input: String) {
        if (input == state.value.savedPin) {
            viewModelScope.launch {
                preferencesUseCases.enableAppLock(false)
                preferencesUseCases.setAppLockPin("")
                _eventFlow.emit(UiEvent.SavePin)
            }
        } else {
            onInputPinError(
                step = EnterPinStep.ENTER_PIN,
                message = "Incorrect PIN. Please try again."
            )
        }
    }

    private fun savePin(pin: String) {
        viewModelScope.launch {
            preferencesUseCases.setAppLockPin(pin)
            preferencesUseCases.enableAppLock(true)
            _eventFlow.emit(UiEvent.SavePin)
        }
    }

    private fun onInputPinError(step: EnterPinStep, message: String) {
        _state.update {
            it.copy(
                step = step,
                currentInput = "",
                errorMessage = message
            )
        }
    }

    sealed class UiEvent {
        object SavePin : UiEvent()
        object NavigateBack : UiEvent()
    }
}
