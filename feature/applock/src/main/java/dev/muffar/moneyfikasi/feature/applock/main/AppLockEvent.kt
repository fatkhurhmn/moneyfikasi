package dev.muffar.moneyfikasi.feature.applock.main

sealed class AppLockEvent {
    data class AppLockEnabledChanged(val isEnabled: Boolean) : AppLockEvent()
    data class BiometricEnabledChanged(val isEnabled: Boolean) : AppLockEvent()
}
