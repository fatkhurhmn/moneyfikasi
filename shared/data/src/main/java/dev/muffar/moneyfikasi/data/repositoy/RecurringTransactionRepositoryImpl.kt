package dev.muffar.moneyfikasi.data.repositoy

import dev.muffar.moneyfikasi.data.db.dao.RecurringTransactionDao
import dev.muffar.moneyfikasi.data.mapper.toDomain
import dev.muffar.moneyfikasi.data.mapper.toEntity
import dev.muffar.moneyfikasi.domain.model.RecurringTransaction
import dev.muffar.moneyfikasi.domain.repository.RecurringTransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class RecurringTransactionRepositoryImpl @Inject constructor(
    private val recurringTransactionDao: RecurringTransactionDao
) : RecurringTransactionRepository {
    override fun getAll(): Flow<List<RecurringTransaction>> {
        return recurringTransactionDao.getAll().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getById(id: UUID): RecurringTransaction? {
        return recurringTransactionDao.getById(id)?.toDomain()
    }

    override suspend fun save(recurringTransaction: RecurringTransaction) {
        recurringTransactionDao.upsert(recurringTransaction.toEntity())
    }

    override suspend fun delete(id: UUID) {
        recurringTransactionDao.delete(id)
    }
}
