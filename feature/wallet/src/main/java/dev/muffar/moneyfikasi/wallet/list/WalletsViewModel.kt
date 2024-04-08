package dev.muffar.moneyfikasi.wallet.list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletsViewModel @Inject constructor(
    private val walletUseCases: WalletUseCases,
) : ViewModel() {

    private val _state = mutableStateOf(WalletsState())
    val state: State<WalletsState> = _state

    init {
        loadAllWallets()
    }

    private fun loadAllWallets() {
        viewModelScope.launch {
            walletUseCases.getAllWallets()
                .collectLatest {
                    _state.value = _state.value.copy(wallets = it)
                }
        }
    }
}