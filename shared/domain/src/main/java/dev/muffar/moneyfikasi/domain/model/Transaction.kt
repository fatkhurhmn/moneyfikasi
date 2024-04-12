package dev.muffar.moneyfikasi.domain.model

import org.threeten.bp.LocalDateTime
import java.util.UUID

data class Transaction(
    val id: UUID,
    val walletId: UUID,
    val categoryId: UUID,
    val type: TransactionType,
    val amount: Double,
    val date: LocalDateTime,
    val description: String? = null,
)

data class InvalidTransactionException(override val message: String) : Exception()