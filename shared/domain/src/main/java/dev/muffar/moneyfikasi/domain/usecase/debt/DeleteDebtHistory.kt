package dev.muffar.moneyfikasi.domain.usecase.debt

import dev.muffar.moneyfikasi.domain.repository.DebtRepository
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import java.util.UUID
import kotlin.math.max

class DeleteDebtHistory(
    private val debtRepository: DebtRepository,
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(debtId: UUID, transactionId: UUID) {
        val debt = debtRepository.getDebtById(debtId)
            ?: throw IllegalArgumentException("Debt not found")
        val existing = transactionRepository.getTransactionById(transactionId)
            ?: throw IllegalArgumentException("Transaction not found")

        if (existing.type == debt.type.toTransactionType()) {
            val newTotalAmount = debt.totalAmount - existing.amount
            require(newTotalAmount >= debt.paidAmount) { "Total cannot be less than paid amount" }
            debtRepository.upsertDebt(debt.copy(totalAmount = newTotalAmount))
        } else {
            debtRepository.upsertDebt(
                debt.copy(paidAmount = max(0.0, debt.paidAmount - existing.amount))
            )
        }

        transactionRepository.deleteTransaction(transactionId)
    }
}
