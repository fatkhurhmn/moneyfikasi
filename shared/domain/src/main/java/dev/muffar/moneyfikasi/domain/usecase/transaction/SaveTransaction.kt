package dev.muffar.moneyfikasi.domain.usecase.transaction

import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository

class SaveTransaction(
    private val transactionRepository: TransactionRepository,
) {

    suspend operator fun invoke(transaction: Transaction) {
        transactionRepository.saveTransaction(transaction)
    }
}
