package dev.muffar.moneyfikasi.transaction.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.usecase.category.CategoryUseCases
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import dev.muffar.moneyfikasi.domain.utils.TransactionDateFilter
import dev.muffar.moneyfikasi.utils.extensions.format
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
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
        observeTransactions()
        loadCategories()
        loadWallets()
    }

    fun onEvent(event: TransactionsEvent) {
        when (event) {
            is TransactionsEvent.FloatingActionButtonClicked -> onExpandFabButton(event.isExpanded)
            is TransactionsEvent.FilterChanged -> onFilterChanged(event.filter)
            is TransactionsEvent.LocalDateTimeChanged -> onLocalDateTimeChange(event.localDateTime)
            is TransactionsEvent.DateRangeChanged -> onDateRangeChanged(event.start, event.end)
            is TransactionsEvent.ShowFilterSheet -> onShowFilterSheet(event.show)
            is TransactionsEvent.CategoryFiltered -> onFilterCategories(event.categories)
            is TransactionsEvent.WalletFiltered -> onFilterWallets(event.wallets)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeTransactions() {
        viewModelScope.launch {
            state
                .map {
                    Triple(
                        it.startDateRange to it.endDateRange,
                        it.selectedCategories,
                        it.selectedWallets
                    )
                }
                .distinctUntilChanged()
                .flatMapLatest { (dateRange, categories, wallets) ->
                    transactionUseCases.getAllTransactions(
                        dateRange.first,
                        dateRange.second,
                        categories,
                        wallets
                    )
                }
                .onStart { _state.update { it.copy(isLoading = true) } }
                .collectLatest { transactions ->
                    _state.update {
                        it.copy(
                            transactions = transactions,
                            transactionsByDate = transactions.groupBy { tx ->
                                tx.date.format("yyyy-MM-dd")
                            },
                            isLoading = false
                        )
                    }
                }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryUseCases.getAllCategories(true)
                .collectLatest { categories ->
                    _state.update { state ->
                        state.copy(
                            categories = categories,
                            selectedCategories = state.selectedCategories.ifEmpty { categories.toSet() }
                        )
                    }
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
                            selectedWallets = state.selectedWallets.ifEmpty {
                                wallets.toSet()
                            },
                        )
                    }
                }
        }
    }

    private fun onExpandFabButton(isExpanded: Boolean) {
        _state.update { it.copy(isExpandedFab = isExpanded) }
    }

    private fun onFilterChanged(filter: TransactionDateFilter) {
        _state.update { it.copy(filter = filter) }
    }

    private fun onLocalDateTimeChange(localDateTime: LocalDateTime) {
        _state.update { it.copy(currentLocalDateTime = localDateTime) }
    }

    private fun onDateRangeChanged(start: Long, end: Long) {
        _state.update { it.copy(startDateRange = start, endDateRange = end) }
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

    }
}