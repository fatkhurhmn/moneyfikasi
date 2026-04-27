package dev.muffar.moneyfikasi.feature.applock.set_pin

import dev.muffar.moneyfikasi.domain.model.ErrorMessage

data class SetPinState(
    val step: SetPinStep = SetPinStep.ENTER_PIN,
    val pin: String = "",
    val confirmPin: String = "",
    val currentPin: String = "",
    val isLoading: Boolean = false,
    val error: ErrorMessage = ErrorMessage()
)

enum class SetPinStep {
    ENTER_PIN,
    CONFIRM_PIN
}
