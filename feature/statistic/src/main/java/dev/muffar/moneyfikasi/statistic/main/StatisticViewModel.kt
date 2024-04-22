package dev.muffar.moneyfikasi.statistic.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.usecase.category.CategoryUseCases
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import dev.muffar.moneyfikasi.domain.utils.TransactionFilter
import dev.muffar.moneyfikasi.statistic.main.component.StatisticSheetType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
        loadOverviews()
        loadCategories()
        loadWallets()
    }

    fun onEvent(event: StatisticEvent) {
        when (event) {
            is StatisticEvent.OnDateRangeChanged -> onDateRangeChange(event.start, event.end)
            is StatisticEvent.OnShowBottomSheet -> onShowBottomSheet(event.type)
            is StatisticEvent.OnFilterChanged -> onFilterChanged(event.filter)
        }
    }

    private fun loadOverviews() {
        viewModelScope.launch {
            transactionUseCases.getAllTransactions(
                _state.value.startDateRange,
                _state.value.endDateRange,
                _state.value.categories,
                _state.value.wallets
            )
                .collectLatest { transactions ->
                    val overviewIncome = transactions.filter { it.type == TransactionType.INCOME }
                        .sumOf { it.amount }
                    val overviewExpense = transactions.filter { it.type == TransactionType.EXPENSE }
                        .sumOf { it.amount }
                    _state.update { state ->
                        state.copy(
                            overviewIncome = overviewIncome,
                            overviewExpense = overviewExpense,
                            overviewTotal = overviewIncome - overviewExpense
                        )
                    }
                }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryUseCases.getAllCategories()
                .collectLatest { categories ->
                    _state.update {
                        it.copy(categories = categories.toSet())
                    }
                    loadOverviews()
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
                    loadOverviews()
                }
        }
    }

    private fun onDateRangeChange(start: Long, end: Long) {
        _state.update { state ->
            state.copy(
                startDateRange = start,
                endDateRange = end
            )
        }
        loadOverviews()
    }

    private fun onShowBottomSheet(type: StatisticSheetType?) {
        _state.update {
            it.copy(sheetType = type)
        }
    }

    private fun onFilterChanged(filter: TransactionFilter) {
        _state.update {
            it.copy(filter = filter)
        }
        loadOverviews()
    }
}
