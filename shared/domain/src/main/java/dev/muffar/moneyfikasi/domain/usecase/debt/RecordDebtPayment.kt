package dev.muffar.moneyfikasi.domain.usecase.debt

import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.repository.DebtRepository
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import dev.muffar.moneyfikasi.utils.constants.UUIDConst
import org.threeten.bp.LocalDateTime
import java.util.UUID

class RecordDebtPayment(
    private val debtRepository: DebtRepository,
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(debtId: UUID, amount: Double) {
        require(amount > 0) { "Amount must be greater than 0" }
        val debt = debtRepository.getDebtById(debtId)
            ?: throw IllegalArgumentException("Debt not found")
        val newPaidAmount = debt.paidAmount + amount
        require(newPaidAmount <= debt.totalAmount) { "Paid amount cannot exceed total amount" }

        val paymentType = when (debt.type) {
            DebtType.DEBT -> TransactionType.EXPENSE
            DebtType.RECEIVABLE -> TransactionType.INCOME
        }
        val categoryId = if (debt.type == DebtType.DEBT) {
            UUIDConst.DebtCategoryId
        } else {
            UUIDConst.ReceivableCategoryId
        }

        transactionRepository.addIncomeOrExpense(
            amount = amount,
            type = paymentType,
            date = LocalDateTime.now(),
            note = debt.name,
            walletId = debt.walletId,
            categoryId = categoryId,
            debtId = debt.id
        )
        debtRepository.upsertDebt(
            debt.copy(
                paidAmount = newPaidAmount,
                updatedAt = System.currentTimeMillis()
            )
        )
    }
}
