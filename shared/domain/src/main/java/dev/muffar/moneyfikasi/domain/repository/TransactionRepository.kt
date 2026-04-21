package dev.muffar.moneyfikasi.domain.repository

import androidx.paging.PagingData
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.TransferDetail
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDateTime
import java.util.UUID

interface TransactionRepository {
    fun getAllTransactions(
        startDateRange: Long,
        endDateRange: Long,
        categories: Set<UUID>? = null,
        wallets: Set<UUID>? = null,
    ): Flow<List<Transaction>>

    fun getAllTransactionsPaged(
        startDateRange: Long,
        endDateRange: Long,
        categories: Set<UUID>? = null,
        wallets: Set<UUID>? = null,
    ): Flow<PagingData<Transaction>>

    fun getRecentTransactions(limit: Int): Flow<List<Transaction>>

    fun getIncomeSum(
        startDateRange: Long,
        endDateRange: Long,
        categories: Set<UUID>? = null,
        wallets: Set<UUID>? = null,
    ): Flow<Double>

    fun getExpenseSum(
        startDateRange: Long,
        endDateRange: Long,
        categories: Set<UUID>? = null,
        wallets: Set<UUID>? = null,
    ): Flow<Double>

    fun getNetBalance(
        startDateRange: Long,
        endDateRange: Long,
        categories: Set<UUID>? = null,
        wallets: Set<UUID>? = null,
    ): Flow<Double>

    fun getAllTransactions(query: String): Flow<List<Transaction>>

    fun getTransactionsByWallet(walletId: UUID): Flow<List<Transaction>>

    suspend fun getTransactionById(id: UUID): Transaction?

    suspend fun addIncomeOrExpense(
        amount: Double,
        type: TransactionType, // INCOME or EXPENSE
        date: LocalDateTime,
        note: String?,
        walletId: UUID,
        categoryId: UUID?
    )

    suspend fun updateIncomeOrExpense(
        id: UUID,
        amount: Double,
        type: TransactionType,
        date: LocalDateTime,
        note: String?,
        walletId: UUID,
        categoryId: UUID?
    )

    suspend fun transferFunds(
        sourceWalletId: UUID,
        targetWalletId: UUID,
        amount: Double,
        fee: Double,
        date: LocalDateTime,
        note: String?,
    )

    suspend fun updateTransfer(
        referenceId: UUID,
        sourceWalletId: UUID,
        targetWalletId: UUID,
        amount: Double,
        fee: Double,
        date: LocalDateTime,
        note: String?,
    )

    suspend fun getTransferDetail(transactionId: UUID): TransferDetail?

    suspend fun deleteTransaction(transactionId: UUID)
}
