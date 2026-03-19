package dev.muffar.moneyfikasi.wallet.add_edit

sealed class AddEditWalletEvent {
    data class NameChanged(val name: String) : AddEditWalletEvent()
    data class BalanceChanged(val balance: String) : AddEditWalletEvent()
    data class ColorChanged(val color: Long) : AddEditWalletEvent()
    data class IconChanged(val icon: String) : AddEditWalletEvent()
    data object WalletActivated : AddEditWalletEvent()
    data class ShowDeleteAlert(val showAlert: Boolean) : AddEditWalletEvent()
    data object SaveWallet : AddEditWalletEvent()
    data object DeleteWallet : AddEditWalletEvent()
}