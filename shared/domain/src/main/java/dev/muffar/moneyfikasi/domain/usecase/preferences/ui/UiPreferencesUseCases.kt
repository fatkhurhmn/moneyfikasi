package dev.muffar.moneyfikasi.domain.usecase.preferences.ui

data class UiPreferencesUseCases(
    val isBalanceVisible: IsBalanceVisible,
    val setBalanceVisibility: SetBalanceVisibility,
    val isReportVisible: IsReportVisible,
    val setReportVisibility: SetReportVisibility
)
