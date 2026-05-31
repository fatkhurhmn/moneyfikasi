package dev.muffar.moneyfikasi.domain.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class UiSettings(
    val isBalanceVisible: Boolean = false,
    val isReportVisible: Boolean = false,
    val isQuickTransactionVisible: Boolean = true,
    val isBudgetVisible: Boolean = true,
    val appTheme: AppTheme = AppTheme.SYSTEM,
    val appLanguage: AppLanguage = AppLanguage.SYSTEM,
    val amountInputType: AmountInputType = AmountInputType.CALCULATOR
)
