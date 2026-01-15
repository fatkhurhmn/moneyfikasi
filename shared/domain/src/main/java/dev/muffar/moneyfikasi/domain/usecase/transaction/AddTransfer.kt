package dev.muffar.moneyfikasi.domain.usecase.transaction

import dev.muffar.moneyfikasi.domain.model.InvalidTransactionException
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import org.threeten.bp.LocalDateTime
import java.util.UUID

class AddTransfer(
    private val repository: TransactionRepository,
) {

    @Throws(InvalidTransactionException::class)
    suspend operator fun invoke(
        sourceWalletId: UUID,
        targetWalletId: UUID,
        amount: Double,
        fee: Double,
        date: LocalDateTime,
        note: String?,
        feeCategoryId: UUID?
    ) {
        if (amount == 0.0) {
            throw InvalidTransactionException("Amount cannot be zero")
        }

        if (sourceWalletId == generateEmptyUUID()) {
            throw InvalidTransactionException("Select source wallet please")
        }

        if (targetWalletId == generateEmptyUUID()) {
            throw InvalidTransactionException("Select target wallet please")
        }

        if (sourceWalletId == targetWalletId) {
            throw InvalidTransactionException("Cannot transfer to the same wallet")
        }

        repository.transferFunds(
            sourceWalletId = sourceWalletId,
            targetWalletId = targetWalletId,
            amount = amount,
            fee = fee,
            date = date,
            note = note,
            feeCategoryId = null
        )
    }

    private fun generateEmptyUUID(): UUID {
        return UUID.fromString("00000000-0000-0000-0000-000000000000")
    }
}
