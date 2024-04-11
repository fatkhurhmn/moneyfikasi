package dev.muffar.moneyfikasi.domain.usecase.transaction

import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import java.util.UUID

class GetTransactionById(
    private val transactionRepository: TransactionRepository,
) {

    suspend operator fun invoke(id: UUID): Transaction? {
        return transactionRepository.getTransactionById(id)
    }
}
