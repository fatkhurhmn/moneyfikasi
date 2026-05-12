package dev.muffar.moneyfikasi.domain.model

data class StatisticInsight(
    val highestIncome: Transaction? = null,
    val highestExpense: Transaction? = null,
    val mostFrequentIncomeCategory: CategoryStatistic? = null,
    val mostFrequentExpenseCategory: CategoryStatistic? = null,
)
