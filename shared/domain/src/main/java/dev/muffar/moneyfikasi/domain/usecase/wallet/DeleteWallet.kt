package dev.muffar.moneyfikasi.domain.usecase.wallet

import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.repository.DebtRepository
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import dev.muffar.moneyfikasi.domain.repository.WalletRepository
import kotlinx.coroutines.flow.firstOrNull

class DeleteWallet(
    private val walletRepository: WalletRepository,
    private val debtRepository: DebtRepository,
    private val transactionRepository: TransactionRepository
) {

    suspend operator fun invoke(wallet: Wallet) {
        val debts = debtRepository.getDebtsByWalletId(wallet.id)
        for (debt in debts) {
            val historyIds = transactionRepository
                .getTransactionsByDebtId(debt.id)
                .firstOrNull()
                .orEmpty()
                .map { it.id }
            val idsToDelete = (listOfNotNull(debt.transactionId) + historyIds).distinct()
            for (transactionId in idsToDelete) {
                transactionRepository.deleteTransaction(transactionId)
            }
            debtRepository.deleteDebt(debt)
        }
        walletRepository.deleteWallet(wallet)
    }
}
