package dev.muffar.moneyfikasi.domain.model

enum class DebtType {
    DEBT,
    RECEIVABLE;

    companion object {
        fun fromString(type: String): DebtType {
            return when (type.uppercase()) {
                "HUTANG" -> DEBT
                "PIUTANG" -> RECEIVABLE
                else -> runCatching { valueOf(type.uppercase()) }.getOrDefault(DEBT)
            }
        }
    }

    fun toTransactionType(): TransactionType {
        return when (this) {
            DEBT -> TransactionType.INCOME
            RECEIVABLE -> TransactionType.EXPENSE
        }
    }

    fun toCategoryType(): CategoryType {
        return when (this) {
            DEBT -> CategoryType.INCOME
            RECEIVABLE -> CategoryType.EXPENSE
        }
    }
}
