package dev.muffar.moneyfikasi.domain.usecase.recurring_transaction

import dev.muffar.moneyfikasi.domain.model.RecurringTransaction
import dev.muffar.moneyfikasi.domain.repository.RecurringTransactionRepository

class SaveRecurringTransaction(
    private val repository: RecurringTransactionRepository
) {
    suspend operator fun invoke(recurringTransaction: RecurringTransaction) {
        repository.save(recurringTransaction)
    }
}
