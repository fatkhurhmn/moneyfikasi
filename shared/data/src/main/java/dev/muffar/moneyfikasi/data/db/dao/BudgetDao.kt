package dev.muffar.moneyfikasi.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.muffar.moneyfikasi.data.db.entity.BudgetEntity
import dev.muffar.moneyfikasi.data.db.entity.BudgetWithCategory
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface BudgetDao {
    @Transaction
    @Query("SELECT * FROM budgets")
    fun getAllBudgets(): Flow<List<BudgetWithCategory>>

    @Transaction
    @Query("SELECT * FROM budgets WHERE id = :id")
    suspend fun getBudgetById(id: UUID): BudgetWithCategory?

    @Upsert
    suspend fun upsertBudget(budget: BudgetEntity)

    @Delete
    suspend fun deleteBudget(budget: BudgetEntity)
}
