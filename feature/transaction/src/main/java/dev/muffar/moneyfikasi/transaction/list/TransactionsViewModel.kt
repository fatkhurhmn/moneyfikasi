package dev.muffar.moneyfikasi.transaction.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.model.TransactionFilter
import dev.muffar.moneyfikasi.domain.usecase.category.CategoryUseCases
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import dev.muffar.moneyfikasi.domain.utils.extension.toDateRange
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
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
            is TransactionsEvent.TimeReferenceChanged -> onTimeReferenceChange(event.timeReference)
            is TransactionsEvent.DateRangeChanged -> onDateRangeChange(event.dateRange)
            is TransactionsEvent.ShowFilterSheet -> onShowFilterSheet(event.show)
            is TransactionsEvent.ShowChooseDateSheet -> onShowChooseDateSheet(event.show)
            is TransactionsEvent.ShowCustomDateSheet -> onShowCustomDateSheet(event.show)
            is TransactionsEvent.ResetFilter -> onResetFilter()
            is TransactionsEvent.FilterChanged -> onFilterChange(event.filter)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeTransactions() {
        val transactions = state
            .map { Pair(it.filter, it.dateRange) }
            .distinctUntilChanged()
            .flatMapLatest { (filter, dateRange) ->
                transactionUseCases.getAllTransactionsPaged(
                    dateRange.start,
                    dateRange.end,
                    filter.categories,
                    filter.wallets
                )
            }
            .cachedIn(viewModelScope)

        _state.update { it.copy(transactions = transactions) }
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

    private fun onTimeReferenceChange(dateTime: LocalDateTime) {
        val currentTimePeriod = state.value.dateRange.timePeriod
        _state.update {
            it.copy(
                timeReference = dateTime,
                dateRange = currentTimePeriod.toDateRange(dateTime)
            )
        }
    }

    private fun onDateRangeChange(dateRange: DateRange) {
        _state.update {
            it.copy(
                dateRange = dateRange,
                timeReference = LocalDateTime.now().with(LocalTime.MIN)
            )
        }
    }

    private fun onShowFilterSheet(show: Boolean) {
        _state.update { it.copy(showFilterSheet = show) }
    }

    private fun onShowChooseDateSheet(show: Boolean) {
        _state.update { it.copy(showChooseDateSheet = show) }
    }

    private fun onShowCustomDateSheet(show: Boolean) {
        _state.update { it.copy(showCustomDateSheet = show) }
    }

    private fun onResetFilter() {
        _state.update {
            it.copy(
                filter = TransactionFilter(
                    categories = it.categories.toSet(),
                    wallets = it.wallets.toSet(),
                ),
                isFilterApplied = false
            )
        }
    }

    private fun onFilterChange(filter: TransactionFilter) {
        _state.update {
            val isFilterApplied =
                it.categories.size != filter.categories.size || it.wallets.size != filter.wallets.size
            it.copy(
                filter = filter,
                isFilterApplied = isFilterApplied
            )
        }
    }
}
