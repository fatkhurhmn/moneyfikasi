package dev.muffar.moneyfikasi.domain.usecase.recurring_transaction

import dev.muffar.moneyfikasi.domain.model.RecurringTransaction
import dev.muffar.moneyfikasi.domain.repository.RecurringTransactionRepository
import kotlinx.coroutines.flow.Flow

class GetAllRecurringTransactions(
    private val repository: RecurringTransactionRepository
) {
    operator fun invoke(): Flow<List<RecurringTransaction>> {
        return repository.getAll()
    }
}
