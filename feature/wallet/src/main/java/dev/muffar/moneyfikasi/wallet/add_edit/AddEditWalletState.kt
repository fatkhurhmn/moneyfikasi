package dev.muffar.moneyfikasi.wallet.add_edit

import dev.muffar.moneyfikasi.wallet.add_edit.component.AddEditWalletBottomSheet
import java.util.UUID

data class AddEditWalletState(
    val id : UUID? = null,
    val name: String = "",
    val balance: String = "0",
    val icon: String = "",
    val color: Long = 0,
    val isActive : Boolean = true,
    val bottomSheetType: AddEditWalletBottomSheet? = null,
    val showAlert : Boolean = false
)