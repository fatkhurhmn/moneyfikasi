package dev.muffar.moneyfikasi.domain.usecase.transaction

import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import java.util.UUID

class DeleteTransaction(
    private val transactionRepository: TransactionRepository,
) {

    suspend operator fun invoke(id: UUID, wallet: Wallet) {
        transactionRepository.deleteTransaction(id, wallet)
    }
}
