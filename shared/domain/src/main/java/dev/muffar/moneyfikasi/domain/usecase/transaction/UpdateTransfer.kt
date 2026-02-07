package dev.muffar.moneyfikasi.domain.usecase.transaction

import dev.muffar.moneyfikasi.domain.model.InvalidTransactionException
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import org.threeten.bp.LocalDateTime
import java.util.UUID

class UpdateTransfer(
    private val repository: TransactionRepository,
) {

    @Throws(InvalidTransactionException::class)
    suspend operator fun invoke(
        referenceId: UUID,
        sourceWalletId: UUID,
        targetWalletId: UUID,
        amount: Double,
        fee: Double,
        date: LocalDateTime,
        note: String?
    ) {
        repository.updateTransfer(
            referenceId = referenceId,
            sourceWalletId = sourceWalletId,
            targetWalletId = targetWalletId,
            amount = amount,
            fee = fee,
            date = date,
            note = note
        )
    }
}
