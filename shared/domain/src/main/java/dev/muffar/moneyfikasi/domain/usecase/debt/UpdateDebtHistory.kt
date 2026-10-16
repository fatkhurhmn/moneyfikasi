package dev.muffar.moneyfikasi.domain.usecase.debt

import dev.muffar.moneyfikasi.domain.repository.DebtRepository
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import org.threeten.bp.LocalDateTime
import java.util.UUID

class UpdateDebtHistory(
    private val debtRepository: DebtRepository,
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(
        debtId: UUID,
        transactionId: UUID,
        amount: Double,
        date: LocalDateTime,
        walletId: UUID,
        note: String?
    ) {
        require(amount > 0) { "Amount must be greater than 0" }
        val debt = debtRepository.getDebtById(debtId)
            ?: throw IllegalArgumentException("Debt not found")
        val existing = transactionRepository.getTransactionById(transactionId)
            ?: throw IllegalArgumentException("Transaction not found")
        val delta = amount - existing.amount

        if (existing.type == debt.type.toTransactionType()) {
            val newTotalAmount = debt.totalAmount + delta
            require(newTotalAmount >= debt.paidAmount) { "Total cannot be less than paid amount" }
            debtRepository.upsertDebt(debt.copy(totalAmount = newTotalAmount))
        } else {
            val newPaidAmount = debt.paidAmount + delta
            require(newPaidAmount >= 0) { "Amount must be greater than 0" }
            debtRepository.upsertDebt(debt.copy(paidAmount = newPaidAmount))
        }

        transactionRepository.updateIncomeOrExpense(
            id = transactionId,
            amount = amount,
            type = existing.type,
            date = date,
            note = note,
            walletId = walletId,
            categoryId = existing.category.id,
            debtId = debt.id
        )
    }
}
