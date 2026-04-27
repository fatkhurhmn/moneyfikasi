package dev.muffar.moneyfikasi.applock

import dev.muffar.moneyfikasi.domain.model.ErrorMessage

data class AppLockState(
    val isAppLockEnabled: Boolean = false,
    val pin: String = "",
    val confirmPin: String = "",
    val isLoading: Boolean = false,
    val error: ErrorMessage = ErrorMessage()
)
