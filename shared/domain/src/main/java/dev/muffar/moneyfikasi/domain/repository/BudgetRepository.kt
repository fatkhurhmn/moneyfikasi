package dev.muffar.moneyfikasi.domain.repository

import dev.muffar.moneyfikasi.domain.model.Budget
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface BudgetRepository {
    fun getAllBudgets(): Flow<List<Budget>>
    suspend fun getBudgetById(id: UUID): Budget?
    suspend fun upsertBudget(budget: Budget)
    suspend fun deleteBudget(budget: Budget)
}
