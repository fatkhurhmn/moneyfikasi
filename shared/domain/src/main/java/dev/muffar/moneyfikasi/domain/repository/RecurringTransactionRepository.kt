package dev.muffar.moneyfikasi.domain.repository

import dev.muffar.moneyfikasi.domain.model.RecurringTransaction
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface RecurringTransactionRepository {
    fun getAll(): Flow<List<RecurringTransaction>>
    suspend fun getById(id: UUID): RecurringTransaction?
    suspend fun save(recurringTransaction: RecurringTransaction)
    suspend fun delete(id: UUID)
}
