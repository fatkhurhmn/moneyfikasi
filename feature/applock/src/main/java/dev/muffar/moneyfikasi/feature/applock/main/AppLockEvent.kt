package dev.muffar.moneyfikasi.feature.applock.main

sealed class AppLockEvent {
    data class OnAppLockEnabledChanged(val isEnabled: Boolean) : AppLockEvent()
    data class OnBiometricEnabledChanged(val isEnabled: Boolean) : AppLockEvent()
}
