package dev.muffar.moneyfikasi.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.muffar.moneyfikasi.data.db.entity.RecurringTransactionEntity
import dev.muffar.moneyfikasi.data.db.entity.RecurringTransactionWithDetails
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface RecurringTransactionDao {
    @Transaction
    @Query("SELECT * FROM recurring_transactions")
    fun getAll(): Flow<List<RecurringTransactionWithDetails>>

    @Transaction
    @Query("SELECT * FROM recurring_transactions WHERE id = :id")
    suspend fun getById(id: UUID): RecurringTransactionWithDetails?

    @Upsert
    suspend fun upsert(recurringTransaction: RecurringTransactionEntity)

    @Query("DELETE FROM recurring_transactions WHERE id = :id")
    suspend fun delete(id: UUID)

    @Query("SELECT MIN(nextRun) FROM recurring_transactions WHERE isActive = 1")
    suspend fun getMinNextRun(): Long?
}
