package dev.muffar.moneyfikasi.data.repositoy

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import dev.muffar.moneyfikasi.data.db.dao.TransactionDao
import dev.muffar.moneyfikasi.data.db.dao.WalletDao
import dev.muffar.moneyfikasi.data.db.entity.TransactionEntity
import dev.muffar.moneyfikasi.data.mapper.toDomain
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.TransferDetail
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.threeten.bp.LocalDateTime
import java.util.UUID
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val walletDao: WalletDao
) : TransactionRepository {
    override fun getAllTransactions(
        startDateRange: Long,
        endDateRange: Long,
        categories: Set<UUID>?,
        wallets: Set<UUID>?
    ): Flow<List<Transaction>> {
        return transactionDao.getAllTransactions(
            start = startDateRange,
            end = endDateRange,
            categories = categories,
            wallets = wallets
        ).map { list -> list.map { it.toDomain() } }
    }

    override fun getAllTransactionsPaged(
        startDateRange: Long,
        endDateRange: Long,
        categories: Set<UUID>?,
        wallets: Set<UUID>?
    ): Flow<PagingData<Transaction>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
            ),
            pagingSourceFactory = {
                transactionDao.getAllTransactionsPaged(
                    start = startDateRange,
                    end = endDateRange,
                    categories = categories,
                    wallets = wallets
                )
            }
        ).flow.map { pagingData ->
            pagingData.map {
                it.toDomain().apply {
                    println("Paging: New Transaction emitted ${this.date}")
                }
            }
        }
    }

    override fun getRecentTransactions(limit: Int): Flow<List<Transaction>> {
        return transactionDao.getRecentTransactions(limit)
            .map { list -> list.map { it.toDomain() } }
    }

    override fun getIncomeSum(
        startDateRange: Long,
        endDateRange: Long,
        categories: Set<UUID>?,
        wallets: Set<UUID>?
    ): Flow<Double> {
        return transactionDao.getIncomeSum(startDateRange, endDateRange, categories, wallets)
    }

    override fun getExpenseSum(
        startDateRange: Long,
        endDateRange: Long,
        categories: Set<UUID>?,
        wallets: Set<UUID>?
    ): Flow<Double> {
        return transactionDao.getExpenseSum(startDateRange, endDateRange, categories, wallets)
    }

    override fun getNetBalance(
        startDateRange: Long,
        endDateRange: Long,
        categories: Set<UUID>?,
        wallets: Set<UUID>?
    ): Flow<Double> {
        return transactionDao.getNetBalance(startDateRange, endDateRange, categories, wallets)
    }

    override fun getAllTransactions(query: String): Flow<List<Transaction>> {
        return transactionDao.getAllTransactions(query)
            .map { list -> list.map { it.toDomain() } }
    }

    override fun getTransactionsByWallet(walletId: UUID): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByWallet(walletId)
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getTransactionById(id: UUID): Transaction? {
        return transactionDao.getTransactionWithDetailsById(id)?.toDomain()
    }

    override suspend fun addIncomeOrExpense(
        amount: Double,
        type: TransactionType,
        date: LocalDateTime,
        note: String?,
        walletId: UUID,
        categoryId: UUID?
    ) {

        val entity = TransactionEntity(
            id = UUID.randomUUID(),
            walletId = walletId,
            categoryId = categoryId,
            type = type,
            amount = amount,
            date = date,
            note = note,
            transactionReference = null
        )

        transactionDao.insertIncomeOrExpense(entity)
    }

    override suspend fun updateIncomeOrExpense(
        id: UUID,
        amount: Double,
        type: TransactionType,
        date: LocalDateTime,
        note: String?,
        walletId: UUID,
        categoryId: UUID?
    ) {
        val entity = TransactionEntity(
            id = id,
            walletId = walletId,
            categoryId = categoryId,
            type = type,
            amount = amount,
            date = date,
            note = note,
            transactionReference = null
        )
        transactionDao.updateIncomeOrExpense(entity)
    }

    override suspend fun transferFunds(
        sourceWalletId: UUID,
        targetWalletId: UUID,
        amount: Double,
        fee: Double,
        date: LocalDateTime,
        note: String?,
    ) {
        if (sourceWalletId == targetWalletId) {
            throw IllegalArgumentException("Cannot transfer to the same wallet")
        }

        transactionDao.performTransfer(
            sourceWalletId = sourceWalletId,
            targetWalletId = targetWalletId,
            amount = amount,
            fee = fee,
            date = date,
            note = note,
        )
    }

    override suspend fun updateTransfer(
        referenceId: UUID,
        sourceWalletId: UUID,
        targetWalletId: UUID,
        amount: Double,
        fee: Double,
        date: LocalDateTime,
        note: String?
    ) {
        transactionDao.updateTransfer(
            oldReferenceId = referenceId,
            sourceWalletId = sourceWalletId,
            targetWalletId = targetWalletId,
            amount = amount,
            fee = fee,
            date = date,
            note = note
        )
    }

    override suspend fun getTransferDetail(transactionId: UUID): TransferDetail? {
        val initialTx = transactionDao.getTransactionById(transactionId) ?: return null
        val refId = initialTx.transactionReference ?: return null

        val relatedTxs = transactionDao.getTransactionsByReference(refId)

        val sourceTx = relatedTxs.find { it.type == TransactionType.TRANSFER_OUT }
        val targetTx = relatedTxs.find { it.type == TransactionType.TRANSFER_IN }
        val feeTx = relatedTxs.find { it.type == TransactionType.EXPENSE }

        if (sourceTx == null || targetTx == null) return null

        val sourceWallet = walletDao.getWalletById(sourceTx.walletId)?.toDomain()!!
        val targetWallet = walletDao.getWalletById(targetTx.walletId)?.toDomain()!!

        return TransferDetail(
            referenceId = refId,
            sourceWallet = sourceWallet,
            targetWallet = targetWallet,
            amount = targetTx.amount,
            fee = feeTx?.amount ?: 0.0,
            date = sourceTx.date,
            note = sourceTx.note
        )
    }

    override suspend fun deleteTransaction(transactionId: UUID) {
        transactionDao.deleteTransaction(transactionId)
    }
}
