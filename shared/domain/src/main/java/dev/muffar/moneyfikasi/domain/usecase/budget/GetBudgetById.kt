package dev.muffar.moneyfikasi.domain.usecase.budget

import dev.muffar.moneyfikasi.domain.model.Budget
import dev.muffar.moneyfikasi.domain.repository.BudgetRepository
import java.util.UUID

class GetBudgetById(
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(id: UUID): Budget? {
        return budgetRepository.getBudgetById(id)
    }
}
