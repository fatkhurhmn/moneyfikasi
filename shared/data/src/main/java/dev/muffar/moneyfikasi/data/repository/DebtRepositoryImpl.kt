package dev.muffar.moneyfikasi.data.repository

import dev.muffar.moneyfikasi.data.db.dao.DebtDao
import dev.muffar.moneyfikasi.data.mapper.toDomain
import dev.muffar.moneyfikasi.data.mapper.toEntity
import dev.muffar.moneyfikasi.domain.model.Debt
import dev.muffar.moneyfikasi.domain.repository.DebtRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class DebtRepositoryImpl @Inject constructor(
    private val debtDao: DebtDao
) : DebtRepository {

    override fun getAllDebts(): Flow<List<Debt>> {
        return debtDao.getAllDebts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getDebtById(id: UUID): Debt? {
        return debtDao.getDebtById(id)?.toDomain()
    }

    override suspend fun getDebtByTransactionId(transactionId: UUID): Debt? {
        return debtDao.getDebtByTransactionId(transactionId)?.toDomain()
    }

    override suspend fun getDebtByHistoryTransactionId(transactionId: UUID): Debt? {
        return debtDao.getDebtByHistoryTransactionId(transactionId)?.toDomain()
    }

    override suspend fun getDebtsByWalletId(walletId: UUID): List<Debt> {
        return debtDao.getDebtsByWalletId(walletId).map { it.toDomain() }
    }

    override suspend fun upsertDebt(debt: Debt) {
        debtDao.upsertDebt(debt.toEntity())
    }

    override suspend fun deleteDebt(debt: Debt) {
        debtDao.deleteDebt(debt.toEntity())
    }
}
