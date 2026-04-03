package dev.muffar.moneyfikasi.domain.model

import java.util.UUID

data class Preset(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val amount: Double,
    val type: TransactionType,
    val category: Category,
    val wallet: Wallet,
    val note: String? = null
)
