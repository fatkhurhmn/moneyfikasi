package dev.muffar.moneyfikasi.domain.repository

import dev.muffar.moneyfikasi.domain.model.UiSettings
import kotlinx.coroutines.flow.Flow

interface UiSettingsRepository {
    fun getUiSettings(): Flow<UiSettings>
    suspend fun setBalanceVisibility(isVisible: Boolean)
    suspend fun setReportVisibility(isVisible: Boolean)
}
