package dev.muffar.moneyfikasi.data.repositoy

import dev.muffar.moneyfikasi.data.preferences.UiPreferences
import dev.muffar.moneyfikasi.domain.model.AmountInputType
import dev.muffar.moneyfikasi.domain.model.AppLanguage
import dev.muffar.moneyfikasi.domain.model.AppTheme
import dev.muffar.moneyfikasi.domain.model.UiSettings
import dev.muffar.moneyfikasi.domain.repository.UiSettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UiSettingsRepositoryImpl @Inject constructor(
    private val uiPreferences: UiPreferences
) : UiSettingsRepository {
    override fun getUiSettings(): Flow<UiSettings> = uiPreferences.uiSettings

    override suspend fun setBalanceVisibility(isVisible: Boolean) {
        uiPreferences.setBalanceVisibility(isVisible)
    }

    override suspend fun setReportVisibility(isVisible: Boolean) {
        uiPreferences.setReportVisibility(isVisible)
    }

    override suspend fun setQuickTransactionVisibility(isVisible: Boolean) {
        uiPreferences.setQuickTransactionVisibility(isVisible)
    }

    override suspend fun setBudgetVisibility(isVisible: Boolean) {
        uiPreferences.setBudgetVisibility(isVisible)
    }

    override suspend fun setAppTheme(theme: AppTheme) {
        uiPreferences.setAppTheme(theme)
    }

    override suspend fun setAppLanguage(language: AppLanguage) {
        uiPreferences.setAppLanguage(language)
    }

    override suspend fun setAmountInputType(type: AmountInputType) {
        uiPreferences.setAmountInputType(type)
    }
}
