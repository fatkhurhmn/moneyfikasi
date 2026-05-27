package dev.muffar.moneyfikasi.domain.model

import java.util.UUID

data class ProcessedRecurring(
    val name: String,
    val amount: Double,
    val transactionId: UUID,
    val recurringId: UUID,
    val type: TransactionType,
    val isEnded: Boolean = false
)