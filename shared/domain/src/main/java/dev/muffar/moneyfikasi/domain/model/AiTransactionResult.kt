package dev.muffar.moneyfikasi.domain.model

data class AiTransactionResult(
    val amount: Double,
    val note: String,
    val type: TransactionType
)
