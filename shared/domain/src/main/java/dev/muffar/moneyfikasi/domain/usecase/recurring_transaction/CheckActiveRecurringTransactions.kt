package dev.muffar.moneyfikasi.domain.usecase.recurring_transaction

import dev.muffar.moneyfikasi.domain.repository.RecurringTransactionRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CheckActiveRecurringTransactions @Inject constructor(
    private val repository: RecurringTransactionRepository
) {
    suspend operator fun invoke(): Boolean {
        return repository.getAll().first().any { it.isActive && !it.isEnded }
    }
}
