package dev.muffar.moneyfikasi.domain.usecase.transaction

import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository

class SaveAllTransactions(
    private val transactionRepository: TransactionRepository,
) {

    suspend operator fun invoke(transactions: List<Transaction>) {
        transactionRepository.saveAllTransactions(transactions)
    }
}
