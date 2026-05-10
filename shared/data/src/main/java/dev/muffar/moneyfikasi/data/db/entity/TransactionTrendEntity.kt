package dev.muffar.moneyfikasi.data.db.entity

import dev.muffar.moneyfikasi.domain.model.TransactionType

data class TransactionTrendEntity(
    val date: Long,
    val amount: Double,
    val type: TransactionType
)
