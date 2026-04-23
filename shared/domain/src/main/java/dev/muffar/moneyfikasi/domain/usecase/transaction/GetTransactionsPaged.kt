package dev.muffar.moneyfikasi.domain.usecase.transaction

import androidx.paging.PagingData
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetTransactionsPaged(
    private val repository: TransactionRepository,
) {

    operator fun invoke(
        query: String,
    ): Flow<PagingData<Transaction>> {
        return repository.getAllTransactionsPaged(query)
    }
}
