package dev.muffar.moneyfikasi.domain.usecase.wallet

data class WalletUseCases(
    val getAllWallets: GetAllWallets,
    val getWalletById: GetWalletById,
    val upsertWallet: UpsertWallet,
    val deleteWallet: DeleteWallet,
)