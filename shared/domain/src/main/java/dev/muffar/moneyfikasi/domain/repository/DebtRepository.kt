package dev.muffar.moneyfikasi.domain.repository

import dev.muffar.moneyfikasi.domain.model.Debt
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface DebtRepository {
    fun getAllDebts(): Flow<List<Debt>>
    suspend fun getDebtById(id: UUID): Debt?
    suspend fun upsertDebt(debt: Debt)
    suspend fun deleteDebt(debt: Debt)
}
