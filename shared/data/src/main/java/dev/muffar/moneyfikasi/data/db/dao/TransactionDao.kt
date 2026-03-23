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
        """
        SELECT * FROM transactions 
        WHERE (date BETWEEN :start AND :end) 
        AND (category_id IN (:categories)) 
        AND (wallet_id IN (:wallets)) 
        ORDER BY date DESC
        """
    )
    abstract fun getAllTransactions(
        start: Long,
        end: Long,
        categories: Set<UUID>?,
        wallets: Set<UUID>?
    ): Flow<List<TransactionWithDetails>>

    @Transaction
    @Query(
        """
        SELECT * FROM transactions 
        WHERE type NOT IN ('TRANSFER_IN', 'TRANSFER_OUT')
        ORDER BY date DESC 
        LIMIT :limit
        """
    )
    abstract fun getRecentTransactions(limit: Int): Flow<List<TransactionWithDetails>>

    @Query(
        """
        SELECT COALESCE(SUM(amount), 0.0) FROM transactions 
        WHERE type = 'INCOME' 
        AND (date BETWEEN :start AND :end)
        AND (category_id IN (:categories))
        AND (wallet_id IN (:wallets))
        """
    )
    abstract fun getIncomeSum(
        start: Long,
        end: Long,
        categories: Set<UUID>?,
        wallets: Set<UUID>?
    ): Flow<Double>

    @Query(
        """
        SELECT COALESCE(SUM(amount), 0.0) FROM transactions 
        WHERE type = 'EXPENSE' 
        AND (date BETWEEN :start AND :end)
        AND (category_id IN (:categories))
        AND (wallet_id IN (:wallets))
        """
    )
    abstract fun getExpenseSum(
        start: Long,
        end: Long,
        categories: Set<UUID>?,
        wallets: Set<UUID>?
    ): Flow<Double>

    @Query(
        """
        SELECT 
            COALESCE(SUM(CASE WHEN type = 'INCOME' THEN amount ELSE 0 END), 0.0) - 
            COALESCE(SUM(CASE WHEN type = 'EXPENSE' THEN amount ELSE 0 END), 0.0)
        FROM transactions 
        WHERE (date BETWEEN :start AND :end)
        AND (category_id IN (:categories))
        AND (wallet_id IN (:wallets))
        """
    )
    abstract fun getNetBalance(
        start: Long,
        end: Long,
        categories: Set<UUID>?,
        wallets: Set<UUID>?
    ): Flow<Double>

    @Transaction
    @Query("SELECT * FROM transactions WHERE note LIKE '%' || :query || '%'")
    abstract fun getAllTransactions(query: String): Flow<List<TransactionWithDetails>>

    @Transaction
    @Query("SELECT * FROM transactions WHERE wallet_id = :walletId ORDER BY date DESC")
    abstract fun getTransactionsByWallet(walletId: UUID): Flow<List<TransactionWithDetails>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    abstract suspend fun getTransactionById(id: UUID): TransactionEntity?

    @Query("SELECT * FROM transactions WHERE id = :id")
    abstract suspend fun getTransactionWithDetailsById(id: UUID): TransactionWithDetails?

    @Query("SELECT * FROM transactions WHERE transaction_reference = :refId")
    abstract suspend fun getTransactionsByReference(refId: UUID): List<TransactionEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertTransactionRaw(transaction: TransactionEntity)

    @Update
    protected abstract suspend fun updateTransactionRaw(transaction: TransactionEntity)

    @Delete
    protected abstract suspend fun deleteTransactionRaw(transaction: TransactionEntity)

    @Query("UPDATE wallets SET balance = balance + :amount WHERE id = :walletId")
    protected abstract suspend fun updateWalletBalance(walletId: UUID, amount: Double)


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

    @Transaction
    open suspend fun updateIncomeOrExpense(newEntity: TransactionEntity) {
        val oldEntity = getTransactionById(newEntity.id) ?: return

        // 1. Revert Old Balance
        val revertAmount = if (oldEntity.type == TransactionType.INCOME) {
            -oldEntity.amount
        } else {
            oldEntity.amount
        }
        updateWalletBalance(oldEntity.walletId, revertAmount)

        // 2. Update Row
        updateTransactionRaw(newEntity)

        // 3. Apply New Balance
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

        // 1. Source (Out)
        val sourceTx = TransactionEntity(
            walletId = sourceWalletId,
            categoryId = InitDataSource.TRANSFER_OUT_CATEGORY.id,
            type = TransactionType.TRANSFER_OUT,
            amount = amount,
            date = date,
            note = note,
            transactionReference = referenceId
        )
        insertTransactionRaw(sourceTx)
        updateWalletBalance(sourceWalletId, -amount)

        // 2. Target (In)
        val targetTx = TransactionEntity(
            walletId = targetWalletId,
            categoryId = InitDataSource.TRANSFER_IN_CATEGORY.id,
            type = TransactionType.TRANSFER_IN,
            amount = amount,
            date = date,
            note = note,
            transactionReference = referenceId
        )
        insertTransactionRaw(targetTx)
        updateWalletBalance(targetWalletId, amount)

        // 3. Fee (Expense)
        if (fee > 0.0) {
            val feeTx = TransactionEntity(
                walletId = sourceWalletId,
                categoryId = InitDataSource.TRANSFER_FEE_CATEGORY.id,
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

        val oldSourceTx = oldRows.find { it.type == TransactionType.TRANSFER_OUT }
            ?: throw IllegalStateException("Corrupt Transfer: Missing Source")
        val oldTargetTx = oldRows.find { it.type == TransactionType.TRANSFER_IN }
            ?: throw IllegalStateException("Corrupt Transfer: Missing Target")
        val oldFeeTx = oldRows.find { it.type == TransactionType.EXPENSE }

        // 1. Revert Old Balances
        updateWalletBalance(oldSourceTx.walletId, oldSourceTx.amount) // Refund Source
        updateWalletBalance(oldTargetTx.walletId, -oldTargetTx.amount) // Deduct Target
        if (oldFeeTx != null) {
            updateWalletBalance(oldFeeTx.walletId, oldFeeTx.amount) // Refund Fee
        }

        // 2. Update Source & Target Rows
        val updatedSource = oldSourceTx.copy(
            walletId = sourceWalletId,
            amount = amount,
            date = date,
            note = note
        )
        updateTransactionRaw(updatedSource)

        val updatedTarget = oldTargetTx.copy(
            walletId = targetWalletId,
            amount = amount,
            date = date,
            note = note
        )
        updateTransactionRaw(updatedTarget)

        // 3. Handle Fee Logic (Create, Update, or Delete)
        if (fee > 0) {
            if (oldFeeTx != null) {
                // Update existing fee
                val updatedFee = oldFeeTx.copy(
                    walletId = sourceWalletId,
                    amount = fee,
                    date = date,
                    note = "Fee for transfer"
                )
                updateTransactionRaw(updatedFee)
            } else {
                // Create new fee
                val newFeeTx = TransactionEntity(
                    id = UUID.randomUUID(),
                    walletId = sourceWalletId,
                    categoryId = InitDataSource.TRANSFER_FEE_CATEGORY.id,
                    type = TransactionType.EXPENSE,
                    amount = fee,
                    date = date,
                    note = "Fee for transfer",
                    transactionReference = oldReferenceId
                )
                insertTransactionRaw(newFeeTx)
            }
        } else {
            // Delete existing fee if new fee is 0
            if (oldFeeTx != null) {
                deleteTransactionRaw(oldFeeTx)
            }
        }

        // 4. Apply New Balances
        updateWalletBalance(sourceWalletId, -amount)
        updateWalletBalance(targetWalletId, amount)
        if (fee > 0) {
            updateWalletBalance(sourceWalletId, -fee)
        }
    }


    @Transaction
    open suspend fun deleteTransaction(transactionId: UUID) {
        val targetTx = getTransactionById(transactionId) ?: return

        // Identify if this is a single transaction or part of a transfer group
        val transactionsToDelete = if (targetTx.transactionReference != null) {
            getTransactionsByReference(targetTx.transactionReference)
        } else {
            listOf(targetTx)
        }

        for (tx in transactionsToDelete) {
            // Determine how to fix the balance
            val balanceCorrection = when (tx.type) {
                TransactionType.INCOME, TransactionType.TRANSFER_IN -> -tx.amount
                TransactionType.EXPENSE, TransactionType.TRANSFER_OUT -> tx.amount
            }

            updateWalletBalance(tx.walletId, balanceCorrection)
            deleteTransactionRaw(tx)
        }
    }
}