package dev.muffar.moneyfikasi.domain.repository

import dev.muffar.moneyfikasi.domain.model.Wallet
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface WalletRepository {

    fun getAllWallets(): Flow<List<Wallet>>

    suspend fun getWalletById(id: UUID): Wallet?

    suspend fun upsertWallet(wallet: Wallet)

    suspend fun deleteWallet(wallet: Wallet)
}