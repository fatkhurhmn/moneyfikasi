package dev.muffar.moneyfikasi.domain.usecase.transaction

import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import java.util.UUID

class DeleteTransaction(
    private val repository: TransactionRepository,
) {
    suspend operator fun invoke(id: UUID) {
        repository.deleteTransaction(id)
    }
}
