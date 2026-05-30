package dev.muffar.moneyfikasi.domain.repository

import dev.muffar.moneyfikasi.domain.model.AppLanguage
import dev.muffar.moneyfikasi.domain.model.AppTheme
import dev.muffar.moneyfikasi.domain.model.UiSettings
import kotlinx.coroutines.flow.Flow

interface UiSettingsRepository {
    fun getUiSettings(): Flow<UiSettings>
    suspend fun setBalanceVisibility(isVisible: Boolean)
    suspend fun setReportVisibility(isVisible: Boolean)
    suspend fun setAppTheme(theme: AppTheme)
    suspend fun setAppLanguage(language: AppLanguage)
}
