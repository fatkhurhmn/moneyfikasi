package dev.muffar.moneyfikasi.domain.usecase.debt

import dev.muffar.moneyfikasi.domain.model.Debt
import dev.muffar.moneyfikasi.domain.repository.DebtRepository
import java.util.UUID

class GetDebtById(
    private val debtRepository: DebtRepository
) {
    suspend operator fun invoke(id: UUID): Debt? {
        return debtRepository.getDebtById(id)
    }
}
