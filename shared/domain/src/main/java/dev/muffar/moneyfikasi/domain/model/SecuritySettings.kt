package dev.muffar.moneyfikasi.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SecuritySettings(
    val isAppLockEnabled: Boolean = false,
    val appLockPin: String = "",
    val isBiometricEnabled: Boolean = false
)
