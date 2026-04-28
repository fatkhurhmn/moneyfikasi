package dev.muffar.moneyfikasi.domain.repository

import kotlinx.coroutines.flow.Flow

interface UiSettingsRepository {
    suspend fun setBalanceVisibility(isVisible: Boolean)
    fun isBalanceVisible(): Flow<Boolean>
    suspend fun setReportVisibility(isVisible: Boolean)
    fun isReportVisible(): Flow<Boolean>
}
