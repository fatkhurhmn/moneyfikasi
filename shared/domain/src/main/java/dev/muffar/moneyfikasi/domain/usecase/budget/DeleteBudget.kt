package dev.muffar.moneyfikasi.domain.usecase.budget

import dev.muffar.moneyfikasi.domain.model.Budget
import dev.muffar.moneyfikasi.domain.repository.BudgetRepository

class DeleteBudget(
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(budget: Budget) {
        budgetRepository.deleteBudget(budget)
    }
}
