package dev.muffar.moneyfikasi.transaction.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.usecase.transaction.TransactionUseCases
import dev.muffar.moneyfikasi.utils.format
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
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
        initState()
    }

    private fun initState() {
        viewModelScope.launch {
            transactionUseCases.getAllTransactions()
                .onStart { _state.update { it.copy(isLoading = true) } }
                .onCompletion { _state.update { it.copy(isLoading = false) } }
                .collectLatest { transactions ->
                    val groupingTransactions = transactions.groupBy {
                        it.date.format("yyyy-MM-dd")
                    }
                    _state.update {
                        it.copy(
                            transactions = transactions,
                            transactionsByDate = groupingTransactions
                        )
                    }
                }
        }
    }

    fun onEvent(event: TransactionsEvent) {

    }
}