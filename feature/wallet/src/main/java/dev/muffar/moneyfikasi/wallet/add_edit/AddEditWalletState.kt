package dev.muffar.moneyfikasi.wallet.add_edit

import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.utils.extensions.clearThousandFormat
import dev.muffar.moneyfikasi.wallet.add_edit.component.AddEditWalletBottomSheet
import java.util.UUID

data class AddEditWalletState(
    val id: UUID? = null,
    val name: String = "",
    val balance: String = "0",
    val icon: String = "",
    val color: Long = 0,
    val isActive: Boolean = true,
    val bottomSheetType: AddEditWalletBottomSheet? = null,
    val showAlert: Boolean = false
) {
    val wallet: Wallet
        get() = Wallet(
            id = id ?: UUID.randomUUID(),
            name = name.trim(),
            icon = icon,
            color = color,
            balance = balance.clearThousandFormat().toDouble(),
            isActive = isActive
        )
}