package dev.muffar.moneyfikasi.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.muffar.moneyfikasi.data.db.entity.LoanEntity
import java.util.UUID

@Dao
interface LoanDao {
    @Upsert
    suspend fun save(loan: LoanEntity)

    @Upsert
    suspend fun saveAll(loans: List<LoanEntity>)

    @Query("UPDATE loans SET is_paid_off = :isPaidOff WHERE id = :id")
    suspend fun update(id: UUID, isPaidOff: Boolean)

    @Query("DELETE FROM loans WHERE id = :id")
    suspend fun delete(id: UUID)

    @Query("DELETE FROM loans")
    suspend fun deleteAll()

    @Query("SELECT * FROM loans")
    suspend fun getAll(): List<LoanEntity>

    @Query("SELECT * FROM loans WHERE id = :id")
    suspend fun getById(id: UUID): LoanEntity
}