package dev.muffar.moneyfikasi.domain.usecase.wallet

import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.repository.WalletRepository

class UpsertWallet(
    private val repository: WalletRepository
) {

    suspend operator fun invoke(wallet: Wallet) {
        repository.upsertWallet(wallet)
    }
}