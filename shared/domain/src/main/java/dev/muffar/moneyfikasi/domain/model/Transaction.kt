package dev.muffar.moneyfikasi.domain.model

import org.threeten.bp.LocalDateTime
import java.util.UUID

data class Transaction(
    val id: UUID,
    val category: Category,
    val wallet: Wallet,
    val type: TransactionType,
    val amount: Double,
    val date: LocalDateTime,
    val note: String? = null,
)

data class InvalidTransactionException(override val message: String) : Exception()