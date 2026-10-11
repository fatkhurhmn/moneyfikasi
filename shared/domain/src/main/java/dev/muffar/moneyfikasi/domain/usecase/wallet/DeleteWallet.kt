package dev.muffar.moneyfikasi.domain.usecase.wallet

import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.repository.DebtRepository
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import dev.muffar.moneyfikasi.domain.repository.WalletRepository

class DeleteWallet(
    private val walletRepository: WalletRepository,
    private val debtRepository: DebtRepository,
    private val transactionRepository: TransactionRepository
) {

    suspend operator fun invoke(wallet: Wallet) {
        val debts = debtRepository.getDebtsByWalletId(wallet.id)
        for (debt in debts) {
            debt.transactionId?.let { transactionId ->
                transactionRepository.deleteTransaction(transactionId)
            }
            debtRepository.deleteDebt(debt)
        }
        walletRepository.deleteWallet(wallet)
    }
}