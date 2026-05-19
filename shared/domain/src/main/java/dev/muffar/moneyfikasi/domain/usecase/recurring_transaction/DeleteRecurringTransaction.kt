package dev.muffar.moneyfikasi.domain.usecase.recurring_transaction

import dev.muffar.moneyfikasi.domain.repository.RecurringTransactionRepository
import java.util.UUID

class DeleteRecurringTransaction(
    private val repository: RecurringTransactionRepository
) {
    suspend operator fun invoke(id: UUID) {
        repository.delete(id)
    }
}
