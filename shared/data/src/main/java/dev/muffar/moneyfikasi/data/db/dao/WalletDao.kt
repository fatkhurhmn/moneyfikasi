package dev.muffar.moneyfikasi.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.muffar.moneyfikasi.data.db.entity.WalletEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface WalletDao {
    @Upsert
    suspend fun save(wallet: WalletEntity)

    @Upsert
    suspend fun saveAll(wallets: List<WalletEntity>)

    @Query("UPDATE wallets SET is_active = :isActive WHERE id = :id")
    suspend fun updateWallet(id: UUID, isActive: Boolean)

    @Query("DELETE FROM wallets WHERE id = :id")
    suspend fun delete(id: UUID)

    @Query("DELETE FROM wallets")
    suspend fun deleteAll()

    @Query("SELECT * FROM wallets ORDER BY name")
    fun getAll(): Flow<List<WalletEntity>>

    @Query("SELECT * FROM wallets WHERE id = :id")
    suspend fun getById(id: UUID): WalletEntity?
}