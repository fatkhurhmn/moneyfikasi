package dev.muffar.moneyfikasi.data.preferences

import androidx.datastore.core.DataStore
import dev.muffar.moneyfikasi.domain.model.UiSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UiPreferences @Inject constructor(
    private val dataStore: DataStore<UiSettings>,
) {
    suspend fun setBalanceVisibility(isVisible: Boolean) {
        dataStore.updateData {
            it.copy(isBalanceVisible = isVisible)
        }
    }

    val isBalanceVisible: Flow<Boolean> = dataStore.data.map {
        it.isBalanceVisible
    }

    suspend fun setReportVisibility(isVisible: Boolean) {
        dataStore.updateData {
            it.copy(isReportVisible = isVisible)
        }
    }

    val isReportVisible: Flow<Boolean> = dataStore.data.map {
        it.isReportVisible
    }
}
