package dev.muffar.moneyfikasi.domain.usecase.wallet

import dev.muffar.moneyfikasi.domain.model.InvalidWalletException
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.repository.WalletRepository

class SaveWallet(
    private val walletRepository: WalletRepository
){

    @Throws(InvalidWalletException::class)
    suspend operator fun invoke(wallet: Wallet) {

        if (wallet.name.isEmpty()) {
            throw InvalidWalletException("Wallet name cannot be empty")
        }

        if (wallet.icon.isEmpty()) {
            throw InvalidWalletException("Select an icon")
        }

        if (wallet.color == 0L) {
            throw InvalidWalletException("Select a color")
        }

        walletRepository.saveWallet(wallet)
    }
}