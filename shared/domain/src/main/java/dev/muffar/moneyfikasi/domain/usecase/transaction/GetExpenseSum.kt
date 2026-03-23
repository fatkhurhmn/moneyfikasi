package dev.muffar.moneyfikasi.domain.usecase.transaction

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetExpenseSum(
    private val repository: TransactionRepository,
) {
    operator fun invoke(
        startDateRange: Long,
        endDateRange: Long,
        categories: Set<Category>,
        wallets: Set<Wallet>,
    ): Flow<Double> {
        val categoriesIds = categories.map { it.id }.toSet()
        val walletIds = wallets.map { it.id }.toSet()
        return repository.getExpenseSum(
            startDateRange,
            endDateRange,
            categoriesIds.ifEmpty { null },
            walletIds.ifEmpty { null },
        )
    }
}
