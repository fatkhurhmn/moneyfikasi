package dev.muffar.moneyfikasi.domain.model

data class TransactionTrend(
    val labels: List<String>,
    val incomeValues: List<Double>,
    val expenseValues: List<Double>,
)
