package dev.muffar.moneyfikasi.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.muffar.moneyfikasi.data.db.entity.WalletEntity
import java.util.UUID

@Dao
interface WalletDao {
    @Upsert
    suspend fun save(wallet: WalletEntity)

    @Upsert
    suspend fun saveAll(wallets: List<WalletEntity>)

    @Query("UPDATE wallets SET balance = :balance WHERE id = :id")
    suspend fun update(id: UUID, balance: Double)

    @Query("DELETE FROM wallets WHERE id = :id")
    suspend fun delete(id: UUID)

    @Query("DELETE FROM wallets")
    suspend fun deleteAll()

    @Query("SELECT * FROM wallets WHERE is_active = :isActive")
    suspend fun getAll(isActive : Boolean): List<WalletEntity>

    @Query("SELECT * FROM wallets WHERE id = :id")
    suspend fun getById(id: UUID): WalletEntity
}