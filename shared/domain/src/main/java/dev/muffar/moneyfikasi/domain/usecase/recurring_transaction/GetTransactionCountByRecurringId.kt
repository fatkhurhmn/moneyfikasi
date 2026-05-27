package dev.muffar.moneyfikasi.domain.usecase.recurring_transaction

import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import java.util.UUID

class GetTransactionCountByRecurringId(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(recurringId: UUID): Int {
        return repository.getTransactionCountByRecurringId(recurringId)
    }
}