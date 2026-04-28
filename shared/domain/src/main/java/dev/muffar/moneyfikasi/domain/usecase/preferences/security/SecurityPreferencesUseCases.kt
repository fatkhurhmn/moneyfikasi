package dev.muffar.moneyfikasi.domain.usecase.preferences.security

data class SecurityPreferencesUseCases(
    val isAppLockEnabled: IsAppLockEnabled,
    val enableAppLock: EnableAppLock,
    val getAppLockPin: GetAppLockPin,
    val setAppLockPin: SetAppLockPin,
    val isBiometricEnabled: IsBiometricEnabled,
    val enableBiometric: EnableBiometric
)
