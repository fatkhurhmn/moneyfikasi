package dev.muffar.moneyfikasi.domain.usecase.transaction

import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import org.threeten.bp.LocalDateTime
import java.util.UUID

class AddTransaction(
    private val repository: TransactionRepository,
) {

    suspend operator fun invoke(
        amount: Double,
        type: TransactionType,
        date: LocalDateTime,
        note: String?,
        walletId: UUID,
        categoryId: UUID?
    ) {
        repository.addIncomeOrExpense(
            amount = amount,
            type = type,
            date = date,
            note = note,
            walletId = walletId,
            categoryId = categoryId
        )
    }
}
