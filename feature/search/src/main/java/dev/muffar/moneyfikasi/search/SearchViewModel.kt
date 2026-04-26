package dev.muffar.moneyfikasi.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.endOfDay
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.startOfDay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val transactionUseCases: TransactionUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    init {
        observeSearch()
    }

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.QueryChanged -> onQueryChange(event.query)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeSearch() {
        val transactions = state
            .map { it.searchQuery }
            .distinctUntilChanged()
            .flatMapLatest { query ->
                transactionUseCases.getTransactionsPaged(query ?: "")
            }
            .cachedIn(viewModelScope)

        _state.update { it.copy(transactions = transactions) }
    }

    fun getDailyBalance(date: LocalDateTime): Flow<Double> {
        return transactionUseCases.getNetBalance(
            date.startOfDay(),
            date.endOfDay(),
            emptySet(),
            emptySet()
        )
    }

    private fun onQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }
}
