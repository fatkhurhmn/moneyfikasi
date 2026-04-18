package dev.muffar.moneyfikasi.domain.usecase.budget

import dev.muffar.moneyfikasi.domain.model.Budget
import dev.muffar.moneyfikasi.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow

class GetAllBudgets(
    private val budgetRepository: BudgetRepository
) {
    operator fun invoke(): Flow<List<Budget>> {
        return budgetRepository.getAllBudgets()
    }
}
