package dev.muffar.moneyfikasi.domain.usecase.wallet

import dev.muffar.moneyfikasi.domain.repository.WalletRepository

class DeleteAllWallets(
    private val walletRepository: WalletRepository
){

    suspend operator fun invoke() {
        walletRepository.deleteAllWallets()
    }
}