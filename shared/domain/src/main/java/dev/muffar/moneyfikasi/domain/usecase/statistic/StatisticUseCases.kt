package dev.muffar.moneyfikasi.domain.usecase.statistic

data class StatisticUseCases(
    val getTransactionTrend: GetTransactionTrend,
    val getCategoryStatistics: GetCategoryStatistics,
    val getStatisticInsights: GetStatisticInsights,
)
