package dev.muffar.moneyfikasi.domain.usecase.wallet

import dev.muffar.moneyfikasi.domain.repository.WalletRepository
import java.util.UUID

class UpdateWalletBalance(
    private val walletRepository: WalletRepository
) {

    suspend operator fun invoke(id: UUID, balance: Double) {
        walletRepository.updateBalance(id, balance)
    }
}