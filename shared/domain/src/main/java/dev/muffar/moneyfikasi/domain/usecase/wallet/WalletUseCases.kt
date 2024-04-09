package dev.muffar.moneyfikasi.domain.usecase.wallet

data class WalletUseCases(
    val getAllWallets: GetAllWallets,
    val getWalletById: GetWalletById,
    val saveWallet: SaveWallet,
    val saveAllWallets: SaveAllWallets,
    val updateWallet: UpdateWallet,
    val deleteWallet: DeleteWallet,
    val deleteAllWallets: DeleteAllWallets,
)