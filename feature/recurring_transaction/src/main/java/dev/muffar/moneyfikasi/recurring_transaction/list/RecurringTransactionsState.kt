package dev.muffar.moneyfikasi.recurring_transaction.list

import dev.muffar.moneyfikasi.domain.model.RecurringTransaction
import dev.muffar.moneyfikasi.domain.model.TransactionType

data class RecurringTransactionsState(
    val recurringTransactions: List<RecurringTransaction> = emptyList(),
    val tabs: List<String> = listOf(TransactionType.INCOME.name, TransactionType.EXPENSE.name),
    val isLoading: Boolean = false,
)
