package dev.muffar.moneyfikasi.data.repositoy

import dev.muffar.moneyfikasi.data.db.dao.WalletDao
import dev.muffar.moneyfikasi.data.mapper.mapToEntity
import dev.muffar.moneyfikasi.data.mapper.mapToModel
import dev.muffar.moneyfikasi.data.mapper.toEntity
import dev.muffar.moneyfikasi.data.mapper.toModel
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class WalletRepositoryImpl @Inject constructor(
    private val walletDao: WalletDao,
) : WalletRepository {
    override suspend fun saveWallet(wallet: Wallet) {
        walletDao.save(wallet.toEntity())
    }

    override suspend fun saveAllWallets(wallets: List<Wallet>) {
        walletDao.saveAll(wallets.mapToEntity())
    }

    override suspend fun updateBalance(id: UUID, balance: Double) {
        walletDao.updateBalance(id, balance)
    }

    override suspend fun updateIsActive(id: UUID, isActive: Boolean) {
        walletDao.updateIsActive(id, isActive)
    }

    override suspend fun deleteWallet(id: UUID) {
        walletDao.delete(id)
    }

    override suspend fun deleteAllWallets() {
        walletDao.deleteAll()
    }

    override suspend fun getAllWallets(): Flow<List<Wallet>> {
        return walletDao.getAll().map {
            it.mapToModel()
        }
    }

    override suspend fun getWalletById(id: UUID): Wallet? {
        return walletDao.getById(id)?.toModel()
    }
}