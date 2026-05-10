package dev.muffar.moneyfikasi.domain.model

data class CategoryStatistic(
    val category: Category,
    val amount: Double,
    val percentage: Double,
    val transactionCount: Int,
)
