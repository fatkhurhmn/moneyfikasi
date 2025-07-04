package dev.muffar.moneyfikasi.domain.usecase.transaction

import android.util.Log
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetAllTransactions(
    private val transactionRepository: TransactionRepository,
) {

    suspend operator fun invoke(
        startDateRange: Long,
        endDateRange: Long,
        categories: Set<Category>,
        wallets: Set<Wallet>,
    ): Flow<List<Transaction>> {
        Log.d("GetAllTransactions", "invoke: $startDateRange $endDateRange $categories $wallets")
        val categoriesIds = categories.map { it.id }.toSet()
        val walletIds = wallets.map { it.id }.toSet()
        return transactionRepository.getAllTransactions(
            startDateRange,
            endDateRange,
            categoriesIds.ifEmpty { null },
            walletIds.ifEmpty { null },
        )
    }
}
