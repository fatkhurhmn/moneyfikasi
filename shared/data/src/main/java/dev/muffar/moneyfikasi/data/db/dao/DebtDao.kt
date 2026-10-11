package dev.muffar.moneyfikasi.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import dev.muffar.moneyfikasi.data.db.entity.DebtEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface DebtDao {
    @Query("SELECT * FROM debts ORDER BY created_at DESC")
    fun getAllDebts(): Flow<List<DebtEntity>>

    @Query("SELECT * FROM debts WHERE id = :id")
    suspend fun getDebtById(id: UUID): DebtEntity?

    @Query("SELECT * FROM debts WHERE wallet_id = :walletId")
    suspend fun getDebtsByWalletId(walletId: UUID): List<DebtEntity>

    @Query("SELECT * FROM debts WHERE transaction_id = :transactionId LIMIT 1")
    suspend fun getDebtByTransactionId(transactionId: UUID): DebtEntity?

    @Upsert
    suspend fun upsertDebt(debt: DebtEntity)

    @Delete
    suspend fun deleteDebt(debt: DebtEntity)
}
