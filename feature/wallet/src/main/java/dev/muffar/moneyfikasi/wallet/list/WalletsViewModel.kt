package dev.muffar.moneyfikasi.wallet.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletsViewModel @Inject constructor(
    private val walletUseCases: WalletUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(WalletsState())
    val state = _state.asStateFlow()

    init {
        loadAllWallets()
    }

    private fun loadAllWallets() {
        viewModelScope.launch {
            walletUseCases.getAllWallets()
                .collectLatest {
                    _state.update { state ->
                        state.copy(wallets = it)
                    }
                }
        }
    }
}