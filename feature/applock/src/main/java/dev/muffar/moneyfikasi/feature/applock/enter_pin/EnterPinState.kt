package dev.muffar.moneyfikasi.feature.applock.enter_pin

import dev.muffar.moneyfikasi.domain.model.ErrorMessage

data class EnterPinState(
    val step: EnterPinStep = EnterPinStep.ENTER_PIN,
    val pin: String = "",
    val confirmPin: String = "",
    val currentPin: String = "",
    val isLoading: Boolean = false,
    val error: ErrorMessage = ErrorMessage()
)

enum class EnterPinStep {
    ENTER_PIN,
    CONFIRM_PIN
}
