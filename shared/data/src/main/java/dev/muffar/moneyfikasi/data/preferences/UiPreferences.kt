package dev.muffar.moneyfikasi.data.preferences

import androidx.datastore.core.DataStore
import dev.muffar.moneyfikasi.domain.model.AppLanguage
import dev.muffar.moneyfikasi.domain.model.AppTheme
import dev.muffar.moneyfikasi.domain.model.UiSettings
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UiPreferences @Inject constructor(
    private val dataStore: DataStore<UiSettings>,
) {
    val uiSettings: Flow<UiSettings> = dataStore.data

    suspend fun setBalanceVisibility(isVisible: Boolean) {
        dataStore.updateData {
            it.copy(isBalanceVisible = isVisible)
        }
    }

    suspend fun setReportVisibility(isVisible: Boolean) {
        dataStore.updateData {
            it.copy(isReportVisible = isVisible)
        }
    }

    suspend fun setQuickTransactionVisibility(isVisible: Boolean) {
        dataStore.updateData {
            it.copy(isQuickTransactionVisible = isVisible)
        }
    }

    suspend fun setBudgetVisibility(isVisible: Boolean) {
        dataStore.updateData {
            it.copy(isBudgetVisible = isVisible)
        }
    }

    suspend fun setAppTheme(theme: AppTheme) {
        dataStore.updateData {
            it.copy(appTheme = theme)
        }
    }

    suspend fun setAppLanguage(language: AppLanguage) {
        dataStore.updateData {
            it.copy(appLanguage = language)
        }
    }
}
