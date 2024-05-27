package dev.muffar.moneyfikasi.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.utils.format
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val transactionUseCases: TransactionUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnQueryChange -> onQueryChange(event.query)
        }
    }

    private fun onQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
        viewModelScope.launch {
            transactionUseCases.getTransactions(query)
                .collectLatest { transactions ->
                    val groupingTransactions = transactions.groupBy { it.date.format("yyyy-MM-dd") }
                    _state.update {
                        it.copy(
                            transactions = if (query.isNotEmpty()) transactions else emptyList(),
                            transactionsByDate = if (query.isNotEmpty()) groupingTransactions else emptyMap()
                        )
                    }
                }
        }
    }
}