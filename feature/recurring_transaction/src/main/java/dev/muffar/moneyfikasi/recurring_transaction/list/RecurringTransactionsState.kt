package dev.muffar.moneyfikasi.recurring_transaction.list

import dev.muffar.moneyfikasi.domain.model.RecurringTransaction

data class RecurringTransactionsState(
    val recurringTransactions: List<RecurringTransaction> = emptyList(),
    val isLoading: Boolean = false,
)
