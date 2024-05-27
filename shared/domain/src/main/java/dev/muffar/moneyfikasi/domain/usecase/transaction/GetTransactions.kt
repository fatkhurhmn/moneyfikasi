package dev.muffar.moneyfikasi.domain.usecase.transaction

import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetTransactions(
    private val transactionRepository: TransactionRepository,
) {

    suspend operator fun invoke(
        query: String,
    ): Flow<List<Transaction>> {
        return transactionRepository.getTransactions(query)
    }
}
