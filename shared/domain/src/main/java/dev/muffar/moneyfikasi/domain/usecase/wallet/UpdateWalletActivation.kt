package dev.muffar.moneyfikasi.domain.usecase.wallet

import dev.muffar.moneyfikasi.domain.repository.WalletRepository
import java.util.UUID

class UpdateWalletActivation(
    private val walletRepository: WalletRepository
){

    suspend operator fun invoke(id: UUID, isActive: Boolean) {
        walletRepository.updateIsActive(id, isActive)
    }
}