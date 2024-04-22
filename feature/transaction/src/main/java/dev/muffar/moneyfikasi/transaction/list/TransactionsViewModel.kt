package dev.muffar.moneyfikasi.transaction.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.usecase.category.CategoryUseCases
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import dev.muffar.moneyfikasi.domain.utils.TransactionFilter
import dev.muffar.moneyfikasi.utils.format
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionUseCases: TransactionUseCases,
    private val categoryUseCases: CategoryUseCases,
    private val walletUseCases: WalletUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionsState())
    val state = _state.asStateFlow()

    init {
        loadTransactions()
        loadCategories()
        loadWallets()
    }

    fun onEvent(event: TransactionsEvent) {
        when (event) {
            is TransactionsEvent.OnExpandFabButton -> onExpandFabButton(event.isExpanded)
            is TransactionsEvent.OnFilterChanged -> onFilterChanged(event.filter)
            is TransactionsEvent.OnDateRangeChanged -> onDateRangeChanged(event.start, event.end)
            is TransactionsEvent.OnShowFilterSheet -> onShowFilterSheet(event.show)
            is TransactionsEvent.OnFilterCategories -> onFilterCategories(event.categories)
            is TransactionsEvent.OnFilterWallets -> onFilterWallets(event.wallets)
            is TransactionsEvent.OnSaveFilter -> reloadTransactions()
        }
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            transactionUseCases.getAllTransactions(
                _state.value.startDateRange,
                _state.value.endDateRange,
                _state.value.selectedCategories.toSet(),
                _state.value.selectedWallets.toSet()
            )
                .onStart { _state.update { it.copy(isLoading = true) } }
                .collectLatest { transactions ->
                    val groupingTransactions = transactions.groupBy {
                        it.date.format("yyyy-MM-dd")
                    }
                    _state.update { state ->
                        state.copy(
                            transactions = transactions,
                            transactionsByDate = groupingTransactions,
                            isLoading = false,
                            overviewIncome = transactions.filter { it.type == TransactionType.INCOME }
                                .sumOf { it.amount },
                            overviewExpense = transactions.filter { it.type == TransactionType.EXPENSE }
                                .sumOf { it.amount },
                        )
                    }
                    _state.update { it.copy(overviewTotal = it.overviewIncome - it.overviewExpense) }
                }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryUseCases.getAllCategories()
                .collectLatest { categories ->
                    _state.update {
                        it.copy(
                            categories = categories,
                            selectedCategories = categories.toSet()
                        )
                    }
                    loadTransactions()
                }
        }
    }

    private fun loadWallets() {
        viewModelScope.launch {
            walletUseCases.getAllWallets()
                .collectLatest { wallets ->
                    _state.update { state ->
                        state.copy(
                            wallets = wallets,
                            selectedWallets = wallets.toSet(),
                            totalBalance = wallets.sumOf { it.balance }
                        )
                    }
                    loadTransactions()
                }
        }
    }

    private fun onExpandFabButton(isExpanded: Boolean) {
        _state.update { it.copy(isExpandedFab = isExpanded) }
    }

    private fun onFilterChanged(filter: TransactionFilter) {
        _state.update { it.copy(filter = filter) }
    }

    private fun onDateRangeChanged(start: Long, end: Long) {
        _state.update { it.copy(startDateRange = start, endDateRange = end) }
        loadTransactions()
    }

    private fun onShowFilterSheet(show: Boolean) {
        _state.update { it.copy(showFilterSheet = show) }
    }

    private fun onFilterCategories(categories: Set<Category>) {
        _state.update { it.copy(selectedCategories = categories) }
    }

    private fun onFilterWallets(wallets: Set<Wallet>) {
        _state.update { it.copy(selectedWallets = wallets) }
    }

    private fun reloadTransactions() {
        loadTransactions()
    }
}