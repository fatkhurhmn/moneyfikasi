package dev.muffar.moneyfikasi.statistic.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.usecase.category.CategoryUseCases
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import dev.muffar.moneyfikasi.domain.utils.extension.toDateRange
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class StatisticViewModel @Inject constructor(
    private val transactionUseCases: TransactionUseCases,
    private val categoryUseCases: CategoryUseCases,
    private val walletUseCases: WalletUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(StatisticState())
    val state = _state.asStateFlow()

    init {
        observeTransactions()
        loadWallets()
        loadCategories()
    }

    fun onEvent(event: StatisticEvent) {
        when (event) {
            is StatisticEvent.DateRangeChanged -> onDateRangeChange(event.dateRange)
            is StatisticEvent.TimeReferenceChanged -> onTimeReferenceChange(event.timeReference)
            is StatisticEvent.ShowChooseDateSheet -> onShowChooseDateSheet(event.show)
            is StatisticEvent.ShowCustomDateSheet -> onShowCustomDateSheet(event.show)
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

                    val incomeTransaction = transactions.filter { it.isIncome }
                    val expenseTransaction = transactions.filter { it.isExpense }

                    val overviewIncome = transactions
                        .filter { it.isIncome }
                        .sumOf { it.amount }
                    val overviewExpense = transactions
                        .filter { it.isExpense }
                        .sumOf { it.amount }

                    _state.update { state ->
                        state.copy(
                            incomeTransactions = incomeTransaction,
                            expenseTransactions = expenseTransaction,
                            overviewIncome = overviewIncome,
                            overviewExpense = overviewExpense,
                            overviewNet = overviewIncome - overviewExpense
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
                    _state.update { state ->
                        state.copy(wallets = wallets.toSet())
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

    private fun onShowChooseDateSheet(show: Boolean) {
        _state.update { it.copy(showChooseDateSheet = show) }
    }

    private fun onShowCustomDateSheet(show: Boolean) {
        _state.update { it.copy(showCustomDateSheet = show) }
    }
}
