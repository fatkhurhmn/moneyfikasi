package dev.muffar.moneyfikasi.domain.usecase.recurring_transaction

import dev.muffar.moneyfikasi.domain.model.RecurringTransaction
import dev.muffar.moneyfikasi.domain.repository.RecurringTransactionRepository
import java.util.UUID

class GetRecurringTransactionById(
    private val repository: RecurringTransactionRepository
) {
    suspend operator fun invoke(id: UUID): RecurringTransaction? {
        return repository.getById(id)
    }
}
