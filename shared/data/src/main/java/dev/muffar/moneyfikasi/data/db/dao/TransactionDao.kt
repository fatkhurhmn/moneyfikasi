package dev.muffar.moneyfikasi.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import dev.muffar.moneyfikasi.data.db.entity.CategoryStatisticEntity
import dev.muffar.moneyfikasi.data.db.entity.TransactionEntity
import dev.muffar.moneyfikasi.data.db.entity.TransactionTrendEntity
import dev.muffar.moneyfikasi.data.db.entity.TransactionWithDetails
import dev.muffar.moneyfikasi.data.utils.InitDataSource
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.utils.constants.UUIDConst
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
        AND (:filterCategories = 0 OR category_id IN (:categories)) 
        AND (:filterWallets = 0 OR wallet_id IN (:wallets)) 
        ORDER BY date DESC
        """
    )
    protected abstract fun getAllTransactionsInternal(
        start: Long,
        end: Long,
        categories: Set<UUID>?,
        filterCategories: Boolean,
        wallets: Set<UUID>?,
        filterWallets: Boolean
    ): Flow<List<TransactionWithDetails>>

    fun getAllTransactions(
        start: Long,
        end: Long,
        categories: Set<UUID>?,
        wallets: Set<UUID>?
    ): Flow<List<TransactionWithDetails>> {
        return getAllTransactionsInternal(
            start,
            end,
            categories,
            !categories.isNullOrEmpty(),
            wallets,
            !wallets.isNullOrEmpty()
        )
    }

    @Transaction
    @Query(
        """
        SELECT * FROM transactions 
        WHERE (date BETWEEN :start AND :end) 
        AND (:filterCategories = 0 OR category_id IN (:categories)) 
        AND (:filterWallets = 0 OR wallet_id IN (:wallets)) 
        ORDER BY date DESC
        """
    )
    protected abstract fun getAllTransactionsPagedInternal(
        start: Long,
        end: Long,
        categories: Set<UUID>?,
        filterCategories: Boolean,
        wallets: Set<UUID>?,
        filterWallets: Boolean
    ): PagingSource<Int, TransactionWithDetails>

    fun getAllTransactionsPaged(
        start: Long,
        end: Long,
        categories: Set<UUID>?,
        wallets: Set<UUID>?
    ): PagingSource<Int, TransactionWithDetails> {
        return getAllTransactionsPagedInternal(
            start,
            end,
            categories,
            !categories.isNullOrEmpty(),
            wallets,
            !wallets.isNullOrEmpty()
        )
    }

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
        SELECT TOTAL(amount) FROM transactions 
        WHERE type = 'INCOME' 
        AND (date BETWEEN :start AND :end)
        AND (:filterCategories = 0 OR category_id IN (:categories))
        AND (:filterWallets = 0 OR wallet_id IN (:wallets))
        """
    )
    protected abstract fun getIncomeSumInternal(
        start: Long,
        end: Long,
        categories: Set<UUID>?,
        filterCategories: Boolean,
        wallets: Set<UUID>?,
        filterWallets: Boolean
    ): Flow<Double>

    fun getIncomeSum(
        start: Long,
        end: Long,
        categories: Set<UUID>?,
        wallets: Set<UUID>?
    ): Flow<Double> {
        return getIncomeSumInternal(
            start,
            end,
            categories,
            !categories.isNullOrEmpty(),
            wallets,
            !wallets.isNullOrEmpty()
        )
    }

    @Query(
        """
        SELECT TOTAL(amount) FROM transactions 
        WHERE type = 'EXPENSE' 
        AND (date BETWEEN :start AND :end)
        AND (:filterCategories = 0 OR category_id IN (:categories))
        AND (:filterWallets = 0 OR wallet_id IN (:wallets))
        """
    )
    protected abstract fun getExpenseSumInternal(
        start: Long,
        end: Long,
        categories: Set<UUID>?,
        filterCategories: Boolean,
        wallets: Set<UUID>?,
        filterWallets: Boolean
    ): Flow<Double>

    fun getExpenseSum(
        start: Long,
        end: Long,
        categories: Set<UUID>?,
        wallets: Set<UUID>?
    ): Flow<Double> {
        return getExpenseSumInternal(
            start,
            end,
            categories,
            !categories.isNullOrEmpty(),
            wallets,
            !wallets.isNullOrEmpty()
        )
    }

    @Query(
        """
        SELECT 
            TOTAL(CASE 
                WHEN type IN ('INCOME', 'TRANSFER_IN') THEN amount 
                WHEN type IN ('EXPENSE', 'TRANSFER_OUT') THEN -amount 
                ELSE 0 
            END)
        FROM transactions 
        WHERE (date BETWEEN :start AND :end)
        AND (:filterCategories = 0 OR category_id IN (:categories))
        AND (:filterWallets = 0 OR wallet_id IN (:wallets))
        """
    )
    protected abstract fun getNetBalanceInternal(
        start: Long,
        end: Long,
        categories: Set<UUID>?,
        filterCategories: Boolean,
        wallets: Set<UUID>?,
        filterWallets: Boolean
    ): Flow<Double>

    fun getNetBalance(
        start: Long,
        end: Long,
        categories: Set<UUID>?,
        wallets: Set<UUID>?
    ): Flow<Double> {
        return getNetBalanceInternal(
            start,
            end,
            categories,
            !categories.isNullOrEmpty(),
            wallets,
            !wallets.isNullOrEmpty()
        )
    }

    @Query(
        """
        SELECT 
            c.*, 
            TOTAL(t.amount) as total_amount, 
            COUNT(t.id) as transaction_count
        FROM transactions t
        INNER JOIN categories c ON t.category_id = c.id
        WHERE (t.date BETWEEN :start AND :end)
        AND t.type = :type
        AND (:filterCategories = 0 OR t.category_id IN (:categories))
        AND (:filterWallets = 0 OR t.wallet_id IN (:wallets))
        GROUP BY t.category_id
        ORDER BY total_amount DESC
        LIMIT (CASE WHEN :limit > 0 THEN :limit ELSE -1 END)
        """
    )
    protected abstract fun getCategoryStatisticsInternal(
        start: Long,
        end: Long,
        type: TransactionType,
        categories: Set<UUID>?,
        filterCategories: Boolean,
        wallets: Set<UUID>?,
        filterWallets: Boolean,
        limit: Int
    ): Flow<List<CategoryStatisticEntity>>

    fun getCategoryStatistics(
        start: Long,
        end: Long,
        type: TransactionType,
        categories: Set<UUID>?,
        wallets: Set<UUID>?,
        limit: Int? = null
    ): Flow<List<CategoryStatisticEntity>> {
        return getCategoryStatisticsInternal(
            start,
            end,
            type,
            categories,
            !categories.isNullOrEmpty(),
            wallets,
            !wallets.isNullOrEmpty(),
            limit ?: 0
        )
    }

    @Query(
        """
        SELECT date, amount, type FROM transactions
        WHERE (date BETWEEN :start AND :end)
        AND type IN ('INCOME', 'EXPENSE')
        AND (:filterCategories = 0 OR category_id IN (:categories))
        AND (:filterWallets = 0 OR wallet_id IN (:wallets))
        """
    )
    protected abstract fun getTransactionTrendItemsInternal(
        start: Long,
        end: Long,
        categories: Set<UUID>?,
        filterCategories: Boolean,
        wallets: Set<UUID>?,
        filterWallets: Boolean
    ): Flow<List<TransactionTrendEntity>>

    fun getTransactionTrendItems(
        start: Long,
        end: Long,
        categories: Set<UUID>?,
        wallets: Set<UUID>?
    ): Flow<List<TransactionTrendEntity>> {
        return getTransactionTrendItemsInternal(
            start,
            end,
            categories,
            !categories.isNullOrEmpty(),
            wallets,
            !wallets.isNullOrEmpty()
        )
    }

    @Transaction
    @Query(
        """
        SELECT * FROM transactions 
        WHERE type = :type
        AND (date BETWEEN :start AND :end)
        AND (:filterCategories = 0 OR category_id IN (:categories))
        AND (:filterWallets = 0 OR wallet_id IN (:wallets))
        ORDER BY amount DESC
        LIMIT 1
        """
    )
    protected abstract fun getHighestTransactionInternal(
        start: Long,
        end: Long,
        type: TransactionType,
        categories: Set<UUID>?,
        filterCategories: Boolean,
        wallets: Set<UUID>?,
        filterWallets: Boolean
    ): Flow<TransactionWithDetails?>

    fun getHighestTransaction(
        start: Long,
        end: Long,
        type: TransactionType,
        categories: Set<UUID>?,
        wallets: Set<UUID>?
    ): Flow<TransactionWithDetails?> {
        return getHighestTransactionInternal(
            start,
            end,
            type,
            categories,
            !categories.isNullOrEmpty(),
            wallets,
            !wallets.isNullOrEmpty()
        )
    }

    @Query(
        """
        SELECT 
            c.*, 
            TOTAL(t.amount) as total_amount, 
            COUNT(t.id) as transaction_count
        FROM transactions t
        INNER JOIN categories c ON t.category_id = c.id
        WHERE (t.date BETWEEN :start AND :end)
        AND t.type = :type
        AND (:filterCategories = 0 OR t.category_id IN (:categories))
        AND (:filterWallets = 0 OR t.wallet_id IN (:wallets))
        GROUP BY t.category_id
        ORDER BY transaction_count DESC
        LIMIT 1
        """
    )
    protected abstract fun getMostFrequentCategoryInternal(
        start: Long,
        end: Long,
        type: TransactionType,
        categories: Set<UUID>?,
        filterCategories: Boolean,
        wallets: Set<UUID>?,
        filterWallets: Boolean
    ): Flow<CategoryStatisticEntity?>

    fun getMostFrequentCategory(
        start: Long,
        end: Long,
        type: TransactionType,
        categories: Set<UUID>?,
        wallets: Set<UUID>?
    ): Flow<CategoryStatisticEntity?> {
        return getMostFrequentCategoryInternal(
            start,
            end,
            type,
            categories,
            !categories.isNullOrEmpty(),
            wallets,
            !wallets.isNullOrEmpty()
        )
    }

    @Transaction
    @Query("SELECT * FROM transactions WHERE note LIKE '%' || :query || '%'")
    abstract fun getAllTransactions(query: String): Flow<List<TransactionWithDetails>>

    @Transaction
    @Query("SELECT * FROM transactions WHERE note LIKE '%' || :query || '%'")
    abstract fun getAllTransactionsPaged(query: String): PagingSource<Int, TransactionWithDetails>

    @Transaction
    @Query("SELECT * FROM transactions WHERE wallet_id = :walletId ORDER BY date DESC")
    abstract fun getTransactionsByWallet(walletId: UUID): Flow<List<TransactionWithDetails>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    abstract suspend fun getTransactionById(id: UUID): TransactionEntity?

    @Query("SELECT * FROM transactions WHERE id = :id")
    abstract suspend fun getTransactionWithDetailsById(id: UUID): TransactionWithDetails?

    @Query("SELECT * FROM transactions WHERE transaction_reference = :refId")
    abstract suspend fun getTransactionsByReference(refId: UUID): List<TransactionEntity>

    @Query("SELECT COUNT(*) FROM transactions WHERE recurring_transaction_id = :recurringId")
    abstract suspend fun getTransactionCountByRecurringId(recurringId: UUID): Int


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
            categoryId = UUIDConst.TransferOutCategoryId,
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
            categoryId = UUIDConst.TransferInCategoryId,
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
                categoryId = UUIDConst.TransferFeeCategoryId,
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

        // 2. Update Source \u0026 Target Rows
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
                    categoryId = UUIDConst.TransferFeeCategoryId,
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
