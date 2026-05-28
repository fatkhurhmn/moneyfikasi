package dev.muffar.moneyfikasi.domain.model

import java.util.UUID

data class Preset(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val amount: Double? = null,
    val type: TransactionType,
    val category: Category? = null,
    val wallet: Wallet? = null
)
