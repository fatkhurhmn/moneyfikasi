package dev.muffar.moneyfikasi.recurring_transaction.list

import dev.muffar.moneyfikasi.domain.model.RecurringTransaction

sealed class RecurringTransactionsEvent {
    data class ToggleRecurringTransaction(val recurringTransaction: RecurringTransaction) :
        RecurringTransactionsEvent()
}
