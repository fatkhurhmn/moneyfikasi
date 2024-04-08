package dev.muffar.moneyfikasi.wallet.list

import dev.muffar.moneyfikasi.domain.model.Wallet

data class WalletsState(
    val wallets : List<Wallet> = emptyList(),
)
