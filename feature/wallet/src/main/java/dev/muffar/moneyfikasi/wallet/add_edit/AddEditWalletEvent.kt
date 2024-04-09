package dev.muffar.moneyfikasi.wallet.add_edit

import dev.muffar.moneyfikasi.wallet.add_edit.component.AddEditWalletBottomSheet

sealed class AddEditWalletEvent {
    data class OnNameChange(val name: String) : AddEditWalletEvent()
    data class OnBalanceChange(val balance: String) : AddEditWalletEvent()
    data class OnColorChange(val color: Long) : AddEditWalletEvent()
    data class OnIconChange(val icon: String) : AddEditWalletEvent()
    data object OnIsActiveChange : AddEditWalletEvent()
    data class OnBottomSheetChange(val type: AddEditWalletBottomSheet?) : AddEditWalletEvent()
    data class OnShowAlert(val showAlert: Boolean) : AddEditWalletEvent()
    data object OnSubmitWallet : AddEditWalletEvent()
    data object OnDeleteWallet : AddEditWalletEvent()
}