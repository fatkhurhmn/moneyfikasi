package dev.muffar.moneyfikasi.wallet.add_edit

import dev.muffar.moneyfikasi.wallet.add_edit.component.AddEditWalletBottomSheet

sealed class AddEditWalletEvent {
    data class NameChanged(val name: String) : AddEditWalletEvent()
    data class BalanceChanged(val balance: String) : AddEditWalletEvent()
    data class ColorChanged(val color: Long) : AddEditWalletEvent()
    data class IconChanged(val icon: String) : AddEditWalletEvent()
    data object WalletActivated : AddEditWalletEvent()
    data class BottomSheetChanged(val type: AddEditWalletBottomSheet?) : AddEditWalletEvent()
    data class ShowDeleteAlert(val showAlert: Boolean) : AddEditWalletEvent()
    data object SaveWallet : AddEditWalletEvent()
    data object DeleteWallet : AddEditWalletEvent()
}