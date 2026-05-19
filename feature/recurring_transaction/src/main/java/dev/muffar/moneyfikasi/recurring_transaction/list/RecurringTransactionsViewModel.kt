package dev.muffar.moneyfikasi.recurring_transaction.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.usecase.recurring_transaction.RecurringTransactionUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecurringTransactionsViewModel @Inject constructor(
    private val recurringTransactionUseCases: RecurringTransactionUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(RecurringTransactionsState())
    val state = _state.asStateFlow()

    init {
        loadAllRecurringTransactions()
    }

    private fun loadAllRecurringTransactions() {
        viewModelScope.launch {
            recurringTransactionUseCases.getAllRecurringTransactions()
                .collectLatest { recurringTransactions ->
                    _state.update { state ->
                        state.copy(recurringTransactions = recurringTransactions)
                    }
                }
        }
    }
}
