package dev.muffar.moneyfikasi.applock.enter_pin

sealed class EnterPinEvent {
    data class PinChanged(val pin: String) : EnterPinEvent()
}
