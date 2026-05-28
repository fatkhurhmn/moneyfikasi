package dev.muffar.moneyfikasi.recurring_transaction.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.RecurringEndType
import dev.muffar.moneyfikasi.domain.model.RecurringTransaction
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

    fun onEvent(event: RecurringTransactionsEvent) {
        when (event) {
            is RecurringTransactionsEvent.ToggleRecurringTransaction -> onToggleRecurringTransaction(
                event.recurringTransaction
            )
        }
    }

    private fun loadAllRecurringTransactions() {
        viewModelScope.launch {
            recurringTransactionUseCases.getAllRecurringTransactions()
                .collectLatest { recurringTransactions ->
                    val updatedList = recurringTransactions.map { recurring ->
                        val count = recurringTransactionUseCases.getTransactionCountByRecurringId(recurring.id)
                        val isEnded = when (recurring.endType) {
                            RecurringEndType.NEVER -> false
                            RecurringEndType.ON_DATE -> {
                                val nextRun = recurring.nextRun ?: recurring.startDate
                                recurring.endDate?.let { nextRun > it } ?: false
                            }

                            RecurringEndType.AFTER_OCCURRENCES -> {
                                recurring.occurrenceCount?.let { count >= it } ?: false
                            }
                        }
                        recurring.copy(isEnded = isEnded, executedCount = count)
                    }
                    _state.update { state ->
                        state.copy(recurringTransactions = updatedList)
                    }
                }
        }
    }

    private fun onToggleRecurringTransaction(recurringTransaction: RecurringTransaction) {
        viewModelScope.launch {
            val updatedRecurringTransaction = recurringTransaction.copy(
                isActive = !recurringTransaction.isActive
            )
            recurringTransactionUseCases.saveRecurringTransaction(updatedRecurringTransaction)
        }
    }
}
