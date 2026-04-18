package dev.muffar.moneyfikasi.domain.usecase.budget

data class BudgetUseCases(
    val getAllBudgets: GetAllBudgets,
    val getBudgetById: GetBudgetById,
    val upsertBudget: UpsertBudget,
    val deleteBudget: DeleteBudget
)
