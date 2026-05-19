package dev.muffar.moneyfikasi.domain.model

enum class TransactionType(val value: String) {
    EXPENSE("Expense"),
    INCOME("Income"),
    TRANSFER_IN("Transfer In"),
    TRANSFER_OUT("Transfer Out");

    companion object {
        fun fromString(type: String): TransactionType {
            return valueOf(type.uppercase())
        }
    }

    fun toCategoryType(): CategoryType {
        return when (this) {
            INCOME, TRANSFER_IN -> CategoryType.INCOME
            else -> CategoryType.EXPENSE
        }
    }
}