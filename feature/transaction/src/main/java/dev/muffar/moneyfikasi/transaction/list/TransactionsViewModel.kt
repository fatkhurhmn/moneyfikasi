package dev.muffar.moneyfikasi.transaction.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.model.TransactionFilter
import dev.muffar.moneyfikasi.domain.usecase.category.CategoryUseCases
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import dev.muffar.moneyfikasi.domain.utils.TimePeriod
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
            is TransactionsEvent.FloatingActionButtonClicked -> onFloatActionButtonClick(event.isExpanded)
            is TransactionsEvent.LocalDateTimeChanged -> onLocalDateTimeChange(event.localDateTime)
            is TransactionsEvent.DateRangeChanged -> onDateRangeChange(event.start, event.end)
            is TransactionsEvent.ShowFilterSheet -> onShowFilterSheet(event.show)
            is TransactionsEvent.FilterChanged -> onFilterChange(event.filter)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeTransactions() {
        viewModelScope.launch {
            state
                .map { it.filter }
                .distinctUntilChanged()
                .flatMapLatest { filter ->
                    transactionUseCases.getAllTransactions(
                        filter.dateRange.start,
                        filter.dateRange.end,
                        filter.categories,
                        filter.wallets
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
                            filter = state.filter.copy(categories = categories.toSet()),
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
                            filter = state.filter.copy(wallets = wallets.toSet()),
                        )
                    }
                }
        }
    }

    private fun onFloatActionButtonClick(isExpanded: Boolean) {
        _state.update { it.copy(isExpandedFab = isExpanded) }
    }

    private fun onLocalDateTimeChange(localDateTime: LocalDateTime) {
        _state.update { it.copy(currentLocalDateTime = localDateTime) }
    }

    private fun onDateRangeChange(start: Long, end: Long) {
        _state.update { it.copy(filter = it.filter.copy(dateRange = DateRange(start, end))) }
    }

    private fun onShowFilterSheet(show: Boolean) {
        _state.update { it.copy(showFilterSheet = show) }
    }

    private fun onFilterChange(filter: TransactionFilter) {
        val dateRange =
            (if (filter.timePeriod == TimePeriod.CUSTOM) filter else state.value.filter).dateRange

        _state.update {
            it.copy(filter = filter.copy(dateRange = dateRange))
        }
    }
}