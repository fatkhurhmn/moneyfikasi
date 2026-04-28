package dev.muffar.moneyfikasi.data.repositoy

import dev.muffar.moneyfikasi.data.preferences.AppPreferences
import dev.muffar.moneyfikasi.domain.repository.UiPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UiPreferencesRepositoryImpl @Inject constructor(
    private val appPreferences: AppPreferences
) : UiPreferencesRepository {
    override suspend fun setBalanceVisibility(isVisible: Boolean) {
        appPreferences.ui.setBalanceVisibility(isVisible)
    }

    override fun isBalanceVisible(): Flow<Boolean> = appPreferences.ui.isBalanceVisible

    override suspend fun setReportVisibility(isVisible: Boolean) {
        appPreferences.ui.setReportVisibility(isVisible)
    }

    override fun isReportVisible(): Flow<Boolean> = appPreferences.ui.isReportVisible
}
