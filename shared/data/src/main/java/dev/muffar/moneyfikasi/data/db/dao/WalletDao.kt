package dev.muffar.moneyfikasi.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import dev.muffar.moneyfikasi.data.db.entity.WalletEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface WalletDao {
    @Query("SELECT * FROM wallets ORDER BY LOWER(name) ASC")
    fun getAllWallets(): Flow<List<WalletEntity>>

    @Query("SELECT * FROM wallets WHERE id = :id")
    suspend fun getWalletById(id: UUID): WalletEntity?

    @Upsert
    suspend fun upsertWallet(wallet: WalletEntity)

    @Delete
    suspend fun deleteWallet(wallet: WalletEntity)

    @Query("UPDATE wallets SET balance = balance + :amount WHERE id = :id")
    suspend fun updateWalletBalance(id: UUID, amount: Double)
}