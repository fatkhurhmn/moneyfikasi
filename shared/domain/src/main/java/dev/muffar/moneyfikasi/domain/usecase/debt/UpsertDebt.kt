package dev.muffar.moneyfikasi.domain.usecase.debt

import dev.muffar.moneyfikasi.domain.model.Debt
import dev.muffar.moneyfikasi.domain.repository.DebtRepository

class UpsertDebt(
    private val debtRepository: DebtRepository
) {
    suspend operator fun invoke(debt: Debt) {
        debtRepository.upsertDebt(debt)
    }
}
