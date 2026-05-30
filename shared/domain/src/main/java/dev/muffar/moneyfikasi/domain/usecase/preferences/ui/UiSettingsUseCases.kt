package dev.muffar.moneyfikasi.domain.usecase.preferences.ui

data class UiSettingsUseCases(
    val getUiSettings: GetUiSettings,
    val setBalanceVisibility: SetBalanceVisibility,
    val setReportVisibility: SetReportVisibility,
    val setAllowNotification: SetAllowNotification,
    val setRecurringTransactionNotification: SetRecurringTransactionNotification,
    val setAppTheme: SetAppTheme,
    val setAppLanguage: SetAppLanguage
)
