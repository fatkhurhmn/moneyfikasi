package dev.muffar.moneyfikasi.applock

import dev.muffar.moneyfikasi.domain.model.AppLockType
import dev.muffar.moneyfikasi.domain.model.ErrorMessage

data class AppLockState(
    val appLockType: AppLockType = AppLockType.NONE,
    val isAppLockEnabled: Boolean = false,
    val pin: String = "",
    val confirmPin: String = "",
    val isBiometricAvailable: Boolean = false,
    val isLoading: Boolean = false,
    val error: ErrorMessage = ErrorMessage()
)
