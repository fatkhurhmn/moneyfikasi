package dev.muffar.moneyfikasi.domain.usecase.transaction

import dev.muffar.moneyfikasi.domain.model.InvalidTransactionException
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import java.util.UUID

class SaveTransaction(
    private val transactionRepository: TransactionRepository,
) {

    @Throws(InvalidTransactionException::class)
    suspend operator fun invoke(transaction: Transaction, wallet: Wallet) {
        if (transaction.amount == 0.0) {
            throw InvalidTransactionException("Amount cannot be zero")
        }

        if (transaction.category.id == generateEmptyUUID()) {
            throw InvalidTransactionException("Select category please")
        }

        if (transaction.wallet.id == generateEmptyUUID()) {
            throw InvalidTransactionException("Select wallet please")
        }

        transactionRepository.saveTransaction(transaction, wallet)
    }

    private fun generateEmptyUUID(): UUID {
        return UUID.fromString("00000000-0000-0000-0000-000000000000")
    }
}
