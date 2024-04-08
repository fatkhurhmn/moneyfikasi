package dev.muffar.moneyfikasi.domain.usecase.wallet

import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.repository.WalletRepository

class SaveWallet(
    private val walletRepository: WalletRepository
){

    suspend operator fun invoke(wallet: Wallet) {
        walletRepository.saveWallet(wallet)
    }
}