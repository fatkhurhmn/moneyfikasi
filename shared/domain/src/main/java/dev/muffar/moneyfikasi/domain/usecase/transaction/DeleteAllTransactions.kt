package dev.muffar.moneyfikasi.domain.usecase.transaction

import dev.muffar.moneyfikasi.domain.repository.TransactionRepository

class DeleteAllTransactions(
    private val transactionRepository: TransactionRepository,
) {

    suspend operator fun invoke() {
        transactionRepository.deleteAllTransactions()
    }
}
