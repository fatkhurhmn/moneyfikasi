package dev.muffar.moneyfikasi.domain.model

import java.util.UUID

data class Wallet(
    val id: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000"),
    val name: String = "",
    val icon: String = "",
    val color: Long = 0,
    val balance: Double = 0.0,
    val isActive: Boolean = true
)

class InvalidWalletException(override val message: String) : Exception(message)