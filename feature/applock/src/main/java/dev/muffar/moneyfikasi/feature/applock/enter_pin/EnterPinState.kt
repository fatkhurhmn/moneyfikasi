package dev.muffar.moneyfikasi.feature.applock.enter_pin

import dev.muffar.moneyfikasi.domain.model.EnterPinStep
import dev.muffar.moneyfikasi.domain.model.EnterPinType

data class EnterPinState(
    val type: EnterPinType = EnterPinType.SET_PIN,
    val step: EnterPinStep = EnterPinStep.ENTER_PIN,
    val isBiometricEnabled: Boolean = false,
    val savedPin: String = "",
    val newPin: String = "",
    val currentInput: String = "",
    val isLoading: Boolean = false,
    val errorMessageResId: Int? = null,
)
