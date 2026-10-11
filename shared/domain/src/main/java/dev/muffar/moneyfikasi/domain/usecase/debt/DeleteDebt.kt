package dev.muffar.moneyfikasi.domain.usecase.debt

import dev.muffar.moneyfikasi.domain.model.Debt
import dev.muffar.moneyfikasi.domain.repository.DebtRepository
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository

class DeleteDebt(
    private val debtRepository: DebtRepository,
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(debt: Debt) {
        val existing = debtRepository.getDebtById(debt.id) ?: debt
        existing.transactionId?.let { transactionId ->
            transactionRepository.deleteTransaction(transactionId)
        }
        debtRepository.deleteDebt(existing)
    }
}
