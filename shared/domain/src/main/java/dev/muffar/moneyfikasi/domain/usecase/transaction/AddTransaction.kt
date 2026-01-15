package dev.muffar.moneyfikasi.domain.usecase.transaction

import dev.muffar.moneyfikasi.domain.model.InvalidTransactionException
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import org.threeten.bp.LocalDateTime
import java.util.UUID

class AddTransaction(
    private val repository: TransactionRepository,
) {

    @Throws(InvalidTransactionException::class)
    suspend operator fun invoke(
        amount: Double,
        type: TransactionType,
        date: LocalDateTime,
        note: String?,
        walletId: UUID,
        categoryId: UUID?
    ) {
        if (amount == 0.0) {
            throw InvalidTransactionException("Amount cannot be zero")
        }

        if (categoryId == generateEmptyUUID()) {
            throw InvalidTransactionException("Select category please")
        }

        if (walletId == generateEmptyUUID()) {
            throw InvalidTransactionException("Select wallet please")
        }

        repository.addIncomeOrExpense(
            amount = amount,
            type = type,
            date = date,
            note = note,
            walletId = walletId,
            categoryId = categoryId
        )
    }

    private fun generateEmptyUUID(): UUID {
        return UUID.fromString("00000000-0000-0000-0000-000000000000")
    }
}
