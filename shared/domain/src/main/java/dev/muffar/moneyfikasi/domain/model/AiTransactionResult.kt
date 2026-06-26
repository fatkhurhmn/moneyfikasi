package dev.muffar.moneyfikasi.domain.model

data class AiTransactionResult(
    val amount: Double,
    val note: String,
    val type: TransactionType,
    val category: String? = null,
    val wallet: String? = null,
    val fromWallet: String? = null,
    val toWallet: String? = null
)
