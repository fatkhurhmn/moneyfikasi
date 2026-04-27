package dev.muffar.moneyfikasi.feature.applock.main

sealed class AppLockEvent {
    data class OnAppLockEnabledChanged(val isEnabled: Boolean) : AppLockEvent()
}
