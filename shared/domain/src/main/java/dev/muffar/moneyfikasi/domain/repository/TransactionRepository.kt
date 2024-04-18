package dev.muffar.moneyfikasi.domain.repository

import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.Wallet
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface TransactionRepository {
    suspend fun saveTransaction(transaction: Transaction, wallet: Wallet, newWallet: Wallet?)
    suspend fun saveAllTransactions(transactions: List<Transaction>)
    suspend fun deleteTransaction(id: UUID, wallet: Wallet)
    suspend fun deleteAllTransactions()
    suspend fun getAllTransactions(
        startDateRange: Long,
        endDateRange: Long,
    ): Flow<List<Transaction>>

    suspend fun getTransactionById(id: UUID): Transaction?
}