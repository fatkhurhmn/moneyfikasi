package dev.muffar.moneyfikasi.domain.usecase.debt

import dev.muffar.moneyfikasi.domain.model.Debt
import dev.muffar.moneyfikasi.domain.repository.DebtRepository
import java.util.UUID

class GetDebtByHistoryTransactionId(
    private val debtRepository: DebtRepository
) {
    suspend operator fun invoke(transactionId: UUID): Debt? {
        return debtRepository.getDebtByHistoryTransactionId(transactionId)
    }
}
