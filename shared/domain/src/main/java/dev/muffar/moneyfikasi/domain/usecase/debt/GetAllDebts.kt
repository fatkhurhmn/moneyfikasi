package dev.muffar.moneyfikasi.domain.usecase.debt

import dev.muffar.moneyfikasi.domain.model.Debt
import dev.muffar.moneyfikasi.domain.repository.DebtRepository
import kotlinx.coroutines.flow.Flow

class GetAllDebts(
    private val debtRepository: DebtRepository
) {
    operator fun invoke(): Flow<List<Debt>> {
        return debtRepository.getAllDebts()
    }
}
