package dev.muffar.moneyfikasi.domain.usecase.debt

import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.repository.DebtRepository
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import org.threeten.bp.LocalDateTime
import java.util.UUID
import kotlin.math.max

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
        note: String?,
        isPayment: Boolean? = null
    ) {
        require(amount > 0) { "Amount must be greater than 0" }
        val debt = debtRepository.getDebtById(debtId)
            ?: throw IllegalArgumentException("Debt not found")
        val existing = transactionRepository.getTransactionById(transactionId)
            ?: throw IllegalArgumentException("Transaction not found")
        val wasIncrease = existing.type == debt.type.toTransactionType()
        val targetsPayment = isPayment ?: !wasIncrease

        var newTotalAmount = debt.totalAmount
        var newPaidAmount = debt.paidAmount
        if (wasIncrease) {
            newTotalAmount -= existing.amount
        } else {
            newPaidAmount -= existing.amount
        }
        if (targetsPayment) {
            newPaidAmount += amount
        } else {
            newTotalAmount += amount
        }
        newTotalAmount = max(0.0, newTotalAmount)
        newPaidAmount = max(0.0, newPaidAmount)
        if (wasIncrease || !targetsPayment) {
            require(newTotalAmount >= newPaidAmount) { "Total cannot be less than paid amount" }
        }
        debtRepository.upsertDebt(
            debt.copy(totalAmount = newTotalAmount, paidAmount = newPaidAmount)
        )

        val newType = if (targetsPayment) {
            when (debt.type) {
                DebtType.DEBT -> TransactionType.EXPENSE
                DebtType.RECEIVABLE -> TransactionType.INCOME
            }
        } else {
            debt.type.toTransactionType()
        }
        transactionRepository.updateIncomeOrExpense(
            id = transactionId,
            amount = amount,
            type = newType,
            date = date,
            note = note,
            walletId = walletId,
            categoryId = existing.category.id,
            debtId = debt.id
        )
    }
}
