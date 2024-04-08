package dev.muffar.moneyfikasi.domain.usecase.wallet

import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.repository.WalletRepository

class SaveAllWallets(
    private val walletRepository: WalletRepository
){

    suspend operator fun invoke(wallets: List<Wallet>) {
        walletRepository.saveAllWallets(wallets)
    }
}