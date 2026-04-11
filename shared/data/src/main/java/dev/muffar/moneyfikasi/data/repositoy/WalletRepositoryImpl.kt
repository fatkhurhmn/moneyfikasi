package dev.muffar.moneyfikasi.data.repositoy

import dev.muffar.moneyfikasi.data.db.dao.WalletDao
import dev.muffar.moneyfikasi.data.mapper.toDomain
import dev.muffar.moneyfikasi.data.mapper.toEntity
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class WalletRepositoryImpl @Inject constructor(
    private val walletDao: WalletDao
) : WalletRepository {

    override fun getAllWallets(): Flow<List<Wallet>> {
        return walletDao.getAllWallets().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getWalletById(id: UUID): Wallet? {
        return walletDao.getWalletById(id)?.toDomain()
    }

    override suspend fun upsertWallet(wallet: Wallet) {
        walletDao.upsertWallet(wallet.toEntity())
    }

    override suspend fun deleteWallet(wallet: Wallet) {
        walletDao.deleteWallet(wallet.toEntity())
    }
}