package dev.muffar.moneyfikasi.domain.model

import java.util.UUID

data class Budget(
    val id: UUID = UUID.randomUUID(),
    val name: String = "",
    val amount: Double = 0.0,
    val category: Category? = null,
)
