package dev.muffar.moneyfikasi.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import dev.muffar.moneyfikasi.data.db.entity.TransactionEntity
import dev.muffar.moneyfikasi.data.db.entity.TransactionWithDetails
import dev.muffar.moneyfikasi.data.utils.InitDataSource
import dev.muffar.moneyfikasi.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDateTime
import java.util.UUID

@Dao
abstract class TransactionDao {

    @Transaction
    @Query(
        "SELECT * FROM transactions " +
                "WHERE (date BETWEEN :start AND :end) " +
                "AND (category_id IN (:categories)) " +
                "AND (wallet_id IN (:wallets)) " +
                "ORDER BY date DESC"
    )
    abstract fun getAllTransactions(
        start: Long,
        end: Long,
        categories: Set<UUID>?,
        wallets: Set<UUID>?
    ): Flow<List<TransactionWithDetails>>

    @Transaction
    @Query("SELECT * FROM transactions WHERE note LIKE '%' || :query || '%'")
    abstract fun getAllTransactions(query: String): Flow<List<TransactionWithDetails>>

    @Transaction
    @Query("SELECT * FROM transactions WHERE wallet_id = :walletId ORDER BY date DESC")
    abstract fun getTransactionsByWallet(walletId: UUID): Flow<List<TransactionWithDetails>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertTransactionRaw(transaction: TransactionEntity)

    @Query("UPDATE wallets SET balance = balance + :amount WHERE id = :walletId")
    abstract suspend fun updateWalletBalance(walletId: UUID, amount: Double)

    @Transaction
    open suspend fun insertIncomeOrExpense(transaction: TransactionEntity) {
        insertTransactionRaw(transaction)

        val balanceChange = if (transaction.type == TransactionType.INCOME) {
            transaction.amount
        } else {
            -transaction.amount
        }
        updateWalletBalance(transaction.walletId, balanceChange)
    }

    @Update
    abstract suspend fun updateTransactionRaw(transaction: TransactionEntity)

    @Transaction
    open suspend fun updateIncomeOrExpense(newEntity: TransactionEntity) {
        val oldEntity = getTransactionById(newEntity.id) ?: return

        val revertAmount = if (oldEntity.type == TransactionType.INCOME) {
            -oldEntity.amount
        } else {
            oldEntity.amount
        }
        updateWalletBalance(oldEntity.walletId, revertAmount)

        updateTransactionRaw(newEntity)

        val applyAmount = if (newEntity.type == TransactionType.INCOME) {
            newEntity.amount
        } else {
            -newEntity.amount
        }
        updateWalletBalance(newEntity.walletId, applyAmount)
    }

    @Transaction
    open suspend fun performTransfer(
        sourceWalletId: UUID,
        targetWalletId: UUID,
        amount: Double,
        fee: Double,
        date: LocalDateTime,
        note: String?,
    ) {
        val referenceId = UUID.randomUUID()

        val sourceTx = TransactionEntity(
            walletId = sourceWalletId,
            categoryId = InitDataSource.EXPENSE_TRANSFER_CATEGORY.id,
            type = TransactionType.TRANSFER_OUT,
            amount = amount,
            date = date,
            note = note,
            transactionReference = referenceId
        )
        insertTransactionRaw(sourceTx)
        updateWalletBalance(sourceWalletId, -amount)

        val targetTx = TransactionEntity(
            walletId = targetWalletId,
            categoryId = InitDataSource.INCOME_TRANSFER_CATEGORY.id,
            type = TransactionType.TRANSFER_IN,
            amount = amount,
            date = date,
            note = note,
            transactionReference = referenceId
        )
        insertTransactionRaw(targetTx)
        updateWalletBalance(targetWalletId, amount)

        if (fee > 0.0) {
            val feeTx = TransactionEntity(
                walletId = sourceWalletId,
                categoryId = InitDataSource.ADMIN_TRANSFER_CATEGORY.id,
                type = TransactionType.EXPENSE,
                amount = fee,
                date = date,
                note = "Fee for transfer",
                transactionReference = referenceId
            )
            insertTransactionRaw(feeTx)
            updateWalletBalance(sourceWalletId, -fee)
        }
    }

    @Transaction
    open suspend fun updateTransfer(
        oldReferenceId: UUID,
        sourceWalletId: UUID,
        targetWalletId: UUID,
        amount: Double,
        fee: Double,
        date: LocalDateTime,
        note: String?,
    ) {
        val oldRows = getTransactionsByReference(oldReferenceId)

        for (tx in oldRows) {
            val correction = when (tx.type) {
                TransactionType.TRANSFER_IN -> -tx.amount
                TransactionType.TRANSFER_OUT, TransactionType.EXPENSE -> tx.amount
                else -> 0.0
            }
            updateWalletBalance(tx.walletId, correction)
            deleteTransactionRaw(tx)
        }

        performTransfer(sourceWalletId, targetWalletId, amount, fee, date, note)
    }

    @Query("SELECT * FROM transactions WHERE id = :id")
    abstract suspend fun getTransactionById(id: UUID): TransactionEntity?

    @Query("SELECT * FROM transactions WHERE id = :id")
    abstract suspend fun getTransactionWithDetailsById(id: UUID): TransactionWithDetails?

    @Query("SELECT * FROM transactions WHERE transaction_reference = :refId")
    abstract suspend fun getTransactionsByReference(refId: UUID): List<TransactionEntity>

    @Delete
    abstract suspend fun deleteTransactionRaw(transaction: TransactionEntity)

    @Transaction
    open suspend fun deleteTransaction(transactionId: UUID) {
        val targetTx = getTransactionById(transactionId) ?: return

        val transactionsToDelete = if (targetTx.transactionReference != null) {
            getTransactionsByReference(targetTx.transactionReference)
        } else {
            listOf(targetTx)
        }

        for (tx in transactionsToDelete) {
            val balanceCorrection = when (tx.type) {
                TransactionType.INCOME, TransactionType.TRANSFER_IN -> -tx.amount
                TransactionType.EXPENSE, TransactionType.TRANSFER_OUT -> tx.amount
            }

            updateWalletBalance(tx.walletId, balanceCorrection)

            deleteTransactionRaw(tx)
        }
    }
}