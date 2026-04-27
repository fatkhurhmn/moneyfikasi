package dev.muffar.moneyfikasi.feature.applock.enter_pin

sealed class EnterPinEvent {
    data class OnPinChanged(val pin: String) : EnterPinEvent()
    object OnBackToEnterPin : EnterPinEvent()
    object OnCancel : EnterPinEvent()
}
