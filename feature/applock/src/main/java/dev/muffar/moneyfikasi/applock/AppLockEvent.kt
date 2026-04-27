package dev.muffar.moneyfikasi.applock

sealed class AppLockEvent {
    data class OnAppLockEnabledChanged(val isEnabled: Boolean) : AppLockEvent()
    data class OnPinChanged(val pin: String) : AppLockEvent()
    data class OnConfirmPinChanged(val pin: String) : AppLockEvent()
    object OnSaveAppLock : AppLockEvent()
}
