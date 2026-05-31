package dev.muffar.moneyfikasi.domain.usecase.preferences.ui

data class UiSettingsUseCases(
    val getUiSettings: GetUiSettings,
    val setBalanceVisibility: SetBalanceVisibility,
    val setReportVisibility: SetReportVisibility,
    val setQuickTransactionVisibility: SetQuickTransactionVisibility,
    val setBudgetVisibility: SetBudgetVisibility,
    val setAppTheme: SetAppTheme,
    val setAppLanguage: SetAppLanguage
)
