package dev.muffar.moneyfikasi.transaction.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.domain.utils.TransactionFilter
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsSheetType
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
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionsState())
    val state = _state.asStateFlow()

    init {
        loadTransactions(
            _state.value.startDateRange,
            _state.value.endDateRange
        )
    }

    private fun loadTransactions(startDateRange: Long? = null, endDateRange: Long? = null) {
        viewModelScope.launch {
            transactionUseCases.getAllTransactions(startDateRange, endDateRange)
                .onStart { _state.update { it.copy(isLoading = true) } }
                .collectLatest { transactions ->
                    val groupingTransactions = transactions.groupBy {
                        it.date.format("yyyy-MM-dd")
                    }
                    _state.update {
                        it.copy(
                            transactions = transactions,
                            transactionsByDate = groupingTransactions,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun onEvent(event: TransactionsEvent) {
        when (event) {
            is TransactionsEvent.OnExpandFabButton -> onExpandFabButton(event.isExpanded)
            is TransactionsEvent.OnFilterChanged -> onFilterChanged(event.filter)
            is TransactionsEvent.OnShowBottomSheet -> onShowBottomSheet(event.type)
            is TransactionsEvent.OnDateRangeChanged -> onDateRangeChanged(event.start, event.end)
        }
    }

    private fun onExpandFabButton(isExpanded : Boolean) {
        _state.update { it.copy(isExpandedFab = isExpanded) }
    }

    private fun onFilterChanged(filter: TransactionFilter) {
        _state.update { it.copy(filter = filter) }
        if (filter == TransactionFilter.ALL) {
            loadTransactions()
        }
    }

    private fun onDateRangeChanged(start: Long, end: Long) {
        _state.update { it.copy(startDateRange = start, endDateRange = end) }
        if (_state.value.filter != TransactionFilter.ALL) {
            loadTransactions(_state.value.startDateRange, _state.value.endDateRange)
        }
    }

    private fun onShowBottomSheet(type: TransactionsSheetType?) {
        _state.update { it.copy(sheetType = type) }
    }
}