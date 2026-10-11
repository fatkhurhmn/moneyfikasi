package dev.muffar.moneyfikasi.debt.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.domain.usecase.debt.DebtUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DebtsViewModel @Inject constructor(
    private val debtUseCases: DebtUseCases,
    private val walletUseCases: WalletUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(DebtsState())
    val state: StateFlow<DebtsState> = _state.asStateFlow()

    init {
        loadDebts()
        loadWallets()
    }

    fun onEvent(event: DebtsEvent) {
        when (event) {
            is DebtsEvent.FilterChanged -> onFilterChange(event.type)
        }
    }

    private fun loadDebts() {
        viewModelScope.launch {
            debtUseCases.getAllDebts().collectLatest { debts ->
                _state.update { it.copy(debts = debts) }
            }
        }
    }

    private fun loadWallets() {
        viewModelScope.launch {
            walletUseCases.getAllWallets().collectLatest { wallets ->
                _state.update { it.copy(wallets = wallets) }
            }
        }
    }

    private fun onFilterChange(type: DebtType?) {
        _state.update { it.copy(selectedType = type) }
    }
}
