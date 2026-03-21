package dev.muffar.moneyfikasi.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val walletUseCases: WalletUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    init {
        getWallets()
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

    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.Refresh -> getWallets()
            is DashboardEvent.ToggleBalanceVisibility -> {
                _state.update { it.copy(isBalanceVisible = !it.isBalanceVisible) }
            }
        }
    }
}