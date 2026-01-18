package dev.muffar.moneyfikasi.domain.model

data class TransactionFilter(
    val categories: Set<Category> = emptySet(),
    val wallets: Set<Wallet> = emptySet(),
)