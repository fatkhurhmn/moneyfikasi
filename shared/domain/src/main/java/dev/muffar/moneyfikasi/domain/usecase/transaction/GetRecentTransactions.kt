package dev.muffar.moneyfikasi.domain.usecase.transaction

import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetRecentTransactions(
    private val repository: TransactionRepository,
) {
    operator fun invoke(limit: Int): Flow<List<Transaction>> {
        return repository.getRecentTransactions(limit)
    }
}
