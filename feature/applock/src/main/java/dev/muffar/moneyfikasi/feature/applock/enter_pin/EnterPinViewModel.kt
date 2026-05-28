package dev.muffar.moneyfikasi.feature.applock.enter_pin

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.EnterPinStep
import dev.muffar.moneyfikasi.domain.model.EnterPinType
import dev.muffar.moneyfikasi.domain.usecase.preferences.security.SecuritySettingsUseCases
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.resource.R
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
    private val securitySettingsUseCases: SecuritySettingsUseCases,
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
            val settings = securitySettingsUseCases.getSecuritySettings().first()
            _state.update {
                it.copy(
                    savedPin = settings.appLockPin,
                    isBiometricEnabled = settings.isBiometricEnabled
                )
            }
        }
    }

    fun onEvent(event: EnterPinEvent) {
        when (event) {
            is EnterPinEvent.PinChanged -> onPinChange(event.pin)
        }
    }

    private fun onPinChange(input: String) {
        _state.update { it.copy(currentInput = input, errorMessageResId = null) }
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
                        messageResId = R.string.pins_do_not_match
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
                messageResId = R.string.incorrect_pin
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
                        messageResId = R.string.incorrect_current_pin
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
                        messageResId = R.string.pins_do_not_match
                    )
                }
            }

            else -> {}
        }
    }

    private fun handleDisablePin(input: String) {
        if (input == state.value.savedPin) {
            viewModelScope.launch {
                securitySettingsUseCases.enableAppLock(false)
                securitySettingsUseCases.setAppLockPin("")
                securitySettingsUseCases.enableBiometric(false)
                _eventFlow.emit(UiEvent.SavePin)
            }
        } else {
            onInputPinError(
                step = EnterPinStep.ENTER_PIN,
                messageResId = R.string.incorrect_pin
            )
        }
    }

    private fun savePin(pin: String) {
        viewModelScope.launch {
            securitySettingsUseCases.setAppLockPin(pin)
            securitySettingsUseCases.enableAppLock(true)
            _eventFlow.emit(UiEvent.SavePin)
        }
    }

    private fun onInputPinError(step: EnterPinStep, messageResId: Int) {
        _state.update {
            it.copy(
                step = step,
                currentInput = "",
                errorMessageResId = messageResId
            )
        }
    }

    sealed class UiEvent {
        object SavePin : UiEvent()
        object NavigateBack : UiEvent()
    }
}
