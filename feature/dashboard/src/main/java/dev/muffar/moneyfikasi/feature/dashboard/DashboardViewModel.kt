package dev.muffar.moneyfikasi.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.usecase.category.CategoryUseCases
import dev.muffar.moneyfikasi.domain.usecase.preferences.PreferencesUseCases
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val walletUseCases: WalletUseCases,
    private val categoryUseCases: CategoryUseCases,
    private val transactionUseCases: TransactionUseCases,
    private val preferencesUseCases: PreferencesUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    init {
        getPreferences()
        loadCategories()
        loadWallets()
        observeTransactions()
    }

    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.Refresh -> {}
            is DashboardEvent.ToggleBalanceVisibility -> toggleBalanceVisibility()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeTransactions() {
        viewModelScope.launch {
            state
                .map { Triple(it.dateRange, it.categories, it.wallets) }
                .distinctUntilChanged()
                .flatMapLatest { (dateRange, category, wallets) ->
                    transactionUseCases.getAllTransactions(
                        dateRange.start,
                        dateRange.end,
                        category,
                        wallets
                    )
                }
                .collectLatest { transactions ->
                    val reportBalance = transactions.sumOf { it.amount }
                    val reportIncome = transactions
                        .filter { it.isIncome }
                        .sumOf { it.amount }
                    val reportExpense = transactions
                        .filter { it.isExpense }
                        .sumOf { it.amount }

                    _state.update { state ->
                        state.copy(
                            reportIncome = reportIncome,
                            reportExpense = reportExpense,
                            reportBalance = reportBalance
                        )
                    }
                }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryUseCases.getAllCategories(true)
                .collectLatest { categories ->
                    _state.update {
                        it.copy(categories = categories.toSet())
                    }
                }
        }
    }

    private fun loadWallets() {
        viewModelScope.launch {
            walletUseCases.getAllWallets()
                .collectLatest { wallets ->
                    val totalBalance = wallets.sumOf { it.balance }
                    _state.update { state ->
                        state.copy(
                            totalBalance = totalBalance,
                            wallets = wallets.toSet()
                        )
                    }
                }
        }
    }

    private fun getPreferences() {
        preferencesUseCases.isBalanceVisible()
            .onEach { isVisible ->
                _state.update { it.copy(isBalanceVisible = isVisible) }
            }.launchIn(viewModelScope)
    }

    private fun toggleBalanceVisibility() {
        viewModelScope.launch {
            preferencesUseCases.setBalanceVisibility(!_state.value.isBalanceVisible)
        }
    }
}