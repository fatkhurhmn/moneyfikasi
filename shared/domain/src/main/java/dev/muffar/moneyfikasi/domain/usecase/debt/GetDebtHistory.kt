package dev.muffar.moneyfikasi.domain.usecase.debt

import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.repository.DebtRepository
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID

class GetDebtHistory(
    private val transactionRepository: TransactionRepository,
    private val debtRepository: DebtRepository
) {
    operator fun invoke(debtId: UUID): Flow<List<Transaction>> = flow {
        val principalId = debtRepository.getDebtById(debtId)?.transactionId
        transactionRepository.getTransactionsByDebtId(debtId).collect { transactions ->
            if (principalId != null && transactions.none { it.id == principalId }) {
                val principal = transactionRepository.getTransactionById(principalId)
                if (principal != null) {
                    emit((transactions + principal).sortedByDescending { it.date })
                } else {
                    emit(transactions)
                }
            } else {
                emit(transactions)
            }
        }
    }
}
