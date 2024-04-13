package dev.muffar.moneyfikasi.data.repositoy

import dev.muffar.moneyfikasi.data.db.dao.TransactionDao
import dev.muffar.moneyfikasi.data.mapper.mapToModel
import dev.muffar.moneyfikasi.data.mapper.toEntity
import dev.muffar.moneyfikasi.data.mapper.toModel
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
) : TransactionRepository {

    override suspend fun saveTransaction(transaction: Transaction, wallet: Wallet) {
        transactionDao.saveTransactionAndWallet(
            transaction.toEntity(),
            wallet.toEntity()
        )
    }

    override suspend fun saveAllTransactions(transactions: List<Transaction>) {
        transactionDao.saveAll(transactions.map { it.toEntity() })
    }

    override suspend fun deleteTransaction(id: UUID) {
        transactionDao.delete(id)
    }

    override suspend fun deleteAllTransactions() {
        transactionDao.deleteAll()
    }

    override suspend fun getAllTransactions(): Flow<List<Transaction>> {
        return transactionDao.getAllWithWalletAndCategory().map { it.mapToModel() }
    }

    override suspend fun getTransactionById(id: UUID): Transaction? {
        return transactionDao.getByIdWithWalletAndCategory(id)?.toModel()
    }
}