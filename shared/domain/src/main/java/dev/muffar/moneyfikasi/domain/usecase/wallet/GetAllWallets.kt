package dev.muffar.moneyfikasi.domain.usecase.wallet

import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow

class GetAllWallets(
    private val repository: WalletRepository
) {

    operator fun invoke(): Flow<List<Wallet>> {
        return repository.getAllWallets()
    }
}