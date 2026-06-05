package dev.muffar.moneyfikasi.settings

import dev.muffar.moneyfikasi.domain.model.AmountInputType
import dev.muffar.moneyfikasi.domain.model.AppLanguage
import dev.muffar.moneyfikasi.domain.model.AppTheme

data class SettingsState(
    val appTheme: AppTheme = AppTheme.SYSTEM,
    val appLanguage: AppLanguage = AppLanguage.SYSTEM,
    val amountInputType: AmountInputType = AmountInputType.CALCULATOR
)
