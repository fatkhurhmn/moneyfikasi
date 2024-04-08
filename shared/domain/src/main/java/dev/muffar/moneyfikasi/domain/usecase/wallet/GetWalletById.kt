package dev.muffar.moneyfikasi.domain.usecase.wallet

import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.repository.WalletRepository
import java.util.UUID

class GetWalletById(
    private val walletRepository: WalletRepository
){

    suspend operator fun invoke(id: UUID): Wallet? {
        return walletRepository.getWalletById(id)
    }
}