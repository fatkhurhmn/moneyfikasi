package dev.muffar.moneyfikasi.domain.model

import java.util.UUID

data class Wallet(
    val id: UUID,
    val name: String,
    val icon: String,
    val color: Long,
    val balance: Double,
    val isActive: Boolean
)

class InvalidWalletException(override val message: String) : Exception(message)