package dev.muffar.moneyfikasi.domain.usecase.preferences.ui

data class UiSettingsUseCases(
    val isBalanceVisible: IsBalanceVisible,
    val setBalanceVisibility: SetBalanceVisibility,
    val isReportVisible: IsReportVisible,
    val setReportVisibility: SetReportVisibility
)
