package dev.muffar.moneyfikasi.domain.model

import dev.muffar.moneyfikasi.utils.constants.UUIDConst
import dev.muffar.moneyfikasi.utils.extensions.formatThousand
import java.util.UUID

data class Wallet(
    val id: UUID = UUIDConst.empty,
    val name: String = "",
    val icon: String = "",
    val color: Long = 0,
    val balance: Double = 0.0,
    val isActive: Boolean = true
) {
    val isNotSet: Boolean
        get() = id == UUIDConst.empty

    val displayAvailableBalance: String
        get() = "Available balance: " + if (isNotSet) "-" else balance.formatThousand()
}