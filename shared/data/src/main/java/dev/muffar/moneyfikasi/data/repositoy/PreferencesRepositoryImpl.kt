package dev.muffar.moneyfikasi.data.repositoy

import dev.muffar.moneyfikasi.data.preferences.PreferencesManager
import dev.muffar.moneyfikasi.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow

class PreferencesRepositoryImpl(
    private val preferencesManager: PreferencesManager
) : PreferencesRepository {
    override suspend fun setBalanceVisibility(isVisible: Boolean) {
        preferencesManager.setBalanceVisibility(isVisible)
    }

    override fun isBalanceVisible(): Flow<Boolean> = preferencesManager.isBalanceVisible
}