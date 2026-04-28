package dev.muffar.moneyfikasi.domain.usecase.preferences.security

data class SecuritySettingsUseCases(
    val getSecuritySettings: GetSecuritySettings,
    val enableAppLock: EnableAppLock,
    val setAppLockPin: SetAppLockPin,
    val enableBiometric: EnableBiometric
)
