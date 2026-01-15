package dev.muffar.moneyfikasi.data.repositoy

import dev.muffar.moneyfikasi.data.db.dao.TransactionDao
import dev.muffar.moneyfikasi.data.db.entity.TransactionEntity
import dev.muffar.moneyfikasi.data.mapper.toDomain
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.threeten.bp.LocalDateTime
import java.util.UUID
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
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
        if (type == TransactionType.TRANSFER_IN || type == TransactionType.TRANSFER_OUT) {
            throw IllegalArgumentException("Use transferFunds() for transfers")
        }

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
        feeCategoryId: UUID?
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
            categoryFeeId = feeCategoryId
        )
    }

    override suspend fun deleteTransaction(transactionId: UUID) {
        transactionDao.deleteTransaction(transactionId)
    }
}