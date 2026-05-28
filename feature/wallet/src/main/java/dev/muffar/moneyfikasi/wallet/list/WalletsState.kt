package dev.muffar.moneyfikasi.wallet.list

import dev.muffar.moneyfikasi.domain.model.Wallet

data class WalletsState(
    val balance: Double = 0.0,
    val wallets: List<Wallet> = emptyList(),
)
