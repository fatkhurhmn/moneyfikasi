package dev.muffar.moneyfikasi.domain.repository

import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.Wallet
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface TransactionRepository {
    suspend fun saveTransaction(transaction: Transaction, wallet: Wallet)
    suspend fun saveAllTransactions(transactions: List<Transaction>)
    suspend fun deleteTransaction(id: UUID)
    suspend fun deleteAllTransactions()
    suspend fun getAllTransactions(): Flow<List<Transaction>>
    suspend fun getTransactionById(id: UUID): Transaction?
}