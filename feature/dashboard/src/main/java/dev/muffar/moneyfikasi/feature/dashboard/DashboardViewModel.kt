package dev.muffar.moneyfikasi.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.usecase.preferences.PreferencesUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val walletUseCases: WalletUseCases,
    private val preferencesUseCases: PreferencesUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    init {
        getWallets()
        getPreferences()
    }

    private fun getWallets() {
        walletUseCases.getAllWallets()
            .onEach { wallets ->
                val totalBalance = wallets.sumOf { it.balance }
                _state.update {
                    it.copy(totalBalance = totalBalance)
                }
            }.launchIn(viewModelScope)
    }

    private fun getPreferences() {
        preferencesUseCases.isBalanceVisible()
            .onEach { isVisible ->
                _state.update { it.copy(isBalanceVisible = isVisible) }
            }.launchIn(viewModelScope)
    }

    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.Refresh -> getWallets()
            is DashboardEvent.ToggleBalanceVisibility -> toggleBalanceVisibility()
        }
    }

    private fun toggleBalanceVisibility() {
        viewModelScope.launch {
            preferencesUseCases.setBalanceVisibility(!_state.value.isBalanceVisible)
        }
    }
}