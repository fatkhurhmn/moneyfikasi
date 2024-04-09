package dev.muffar.moneyfikasi.domain.repository

import dev.muffar.moneyfikasi.domain.model.Wallet
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface WalletRepository {
    suspend fun saveWallet(wallet: Wallet)
    suspend fun saveAllWallets(wallets: List<Wallet>)
    suspend fun updateWallet(id: UUID, isActive: Boolean)
    suspend fun deleteWallet(id: UUID)
    suspend fun deleteAllWallets()
    suspend fun getAllWallets(): Flow<List<Wallet>>
    suspend fun getWalletById(id: UUID): Wallet?
}