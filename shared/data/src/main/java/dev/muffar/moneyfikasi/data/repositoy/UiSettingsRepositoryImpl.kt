package dev.muffar.moneyfikasi.data.repositoy

import dev.muffar.moneyfikasi.data.preferences.UiPreferences
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
}
