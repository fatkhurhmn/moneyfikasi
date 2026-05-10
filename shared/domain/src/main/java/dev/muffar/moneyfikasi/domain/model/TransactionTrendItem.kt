package dev.muffar.moneyfikasi.domain.model

import org.threeten.bp.LocalDateTime

data class TransactionTrendItem(
    val date: LocalDateTime,
    val amount: Double,
    val type: TransactionType
) {
    val isIncome: Boolean get() = type == TransactionType.INCOME
    val isExpense: Boolean get() = type == TransactionType.EXPENSE
}
