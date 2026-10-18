package dev.muffar.moneyfikasi.domain.usecase.debt

import dev.muffar.moneyfikasi.domain.model.Debt
import dev.muffar.moneyfikasi.domain.repository.DebtRepository
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.firstOrNull

class DeleteDebt(
    private val debtRepository: DebtRepository,
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(debt: Debt) {
        val existing = debtRepository.getDebtById(debt.id) ?: debt
        val historyIds = transactionRepository
            .getTransactionsByDebtId(existing.id)
            .firstOrNull()
            .orEmpty()
            .map { it.id }
        val idsToDelete = (listOfNotNull(existing.transactionId) + historyIds).distinct()
        for (transactionId in idsToDelete) {
            transactionRepository.deleteTransaction(transactionId)
        }
        debtRepository.deleteDebt(existing)
    }
}
