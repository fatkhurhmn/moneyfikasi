package dev.muffar.moneyfikasi.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.muffar.moneyfikasi.data.db.entity.TransactionEntity
import dev.muffar.moneyfikasi.data.db.entity.TransactionWithWalletAndCategory
import dev.muffar.moneyfikasi.data.db.entity.WalletEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface TransactionDao {

    @Transaction
    suspend fun saveTransactionAndWallet(
        transaction: TransactionEntity,
        wallet: WalletEntity,
        newWallet : WalletEntity?
    ) {
        save(transaction)
        updateWallet(wallet)
        if (newWallet != null) {
            updateWallet(newWallet)
        }
    }

    @Transaction
    suspend fun deleteTransactionAndUpdateWallet(
        transactionId: UUID,
        wallet: WalletEntity
    ) {
        delete(transactionId)
        updateWallet(wallet)
    }

    @Upsert
    suspend fun save(transaction: TransactionEntity)

    @Upsert
    suspend fun saveAll(transactions: List<TransactionEntity>)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun delete(id: UUID)

    @Query("DELETE FROM transactions")
    suspend fun deleteAll()

    @Query("SELECT * FROM transactions")
    fun getAll(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getById(id: UUID): TransactionEntity?

    @Transaction
    @Query("SELECT * FROM transactions WHERE date BETWEEN :start AND :end ORDER BY date DESC")
    fun getAllWithWalletAndCategory(
        start: Long,
        end: Long
    ): Flow<List<TransactionWithWalletAndCategory>>

    @Transaction
    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getByIdWithWalletAndCategory(id: UUID): TransactionWithWalletAndCategory?

    @Upsert
    suspend fun updateWallet(wallet: WalletEntity)
}