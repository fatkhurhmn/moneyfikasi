package dev.muffar.moneyfikasi.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.muffar.moneyfikasi.data.db.entity.TransactionEntity
import java.util.UUID

@Dao
interface TransactionDao {
    @Upsert
    suspend fun save(transaction: TransactionEntity)

    @Upsert
    suspend fun saveAll(transactions: List<TransactionEntity>)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun delete(id: UUID)

    @Query("DELETE FROM transactions")
    suspend fun deleteAll()

    @Query("SELECT * FROM transactions")
    suspend fun getAll(): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getById(id: UUID): TransactionEntity
}