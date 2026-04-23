package dev.muffar.moneyfikasi.domain.usecase.preferences

data class PreferencesUseCases(
    val isBalanceVisible: IsBalanceVisible,
    val setBalanceVisibility: SetBalanceVisibility,
    val isReportVisible: IsReportVisible,
    val setReportVisibility: SetReportVisibility,
    val getLatestBackup: GetLatestBackup,
    val setLatestBackup: SetLatestBackup
)
