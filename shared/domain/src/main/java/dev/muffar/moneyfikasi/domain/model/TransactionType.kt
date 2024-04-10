package dev.muffar.moneyfikasi.domain.model

enum class TransactionType(val value: String) {
    EXPENSE("Expense"),
    INCOME("Income"),
    TRANSFER("Transfer");

    companion object {
        fun fromString(type: String): TransactionType {
            return valueOf(type.uppercase())
        }
    }
}