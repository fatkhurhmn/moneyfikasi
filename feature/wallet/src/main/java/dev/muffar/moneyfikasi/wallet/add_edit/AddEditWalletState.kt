package dev.muffar.moneyfikasi.wallet.add_edit

import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.utils.extensions.clearThousandFormat
import java.util.UUID

data class AddEditWalletState(
    val id: UUID? = null,
    val name: String = "",
    val nameError: ErrorMessage = ErrorMessage(),

    val balance: String = "0",

    val icon: String = "",
    val color: Long = 0,
    val iconError: ErrorMessage = ErrorMessage(),

    val isActive: Boolean = true,
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