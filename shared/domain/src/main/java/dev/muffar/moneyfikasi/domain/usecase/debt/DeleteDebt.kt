package dev.muffar.moneyfikasi.domain.usecase.debt

import dev.muffar.moneyfikasi.domain.model.Debt
import dev.muffar.moneyfikasi.domain.repository.DebtRepository

class DeleteDebt(
    private val debtRepository: DebtRepository
) {
    suspend operator fun invoke(debt: Debt) {
        debtRepository.deleteDebt(debt)
    }
}
