package dev.muffar.moneyfikasi.applock

import dev.muffar.moneyfikasi.domain.model.AppLockType

sealed class AppLockEvent {
    data class OnAppLockEnabledChanged(val isEnabled: Boolean) : AppLockEvent()
    data class OnAppLockTypeChanged(val type: AppLockType) : AppLockEvent()
    data class OnPinChanged(val pin: String) : AppLockEvent()
    data class OnConfirmPinChanged(val pin: String) : AppLockEvent()
    object OnSaveAppLock : AppLockEvent()
}
