package dev.muffar.moneyfikasi.feature.applock.set_pin

sealed class SetPinEvent {
    data class OnPinChanged(val pin: String) : SetPinEvent()
    object OnBackToEnterPin : SetPinEvent()
    object OnCancel : SetPinEvent()
}
