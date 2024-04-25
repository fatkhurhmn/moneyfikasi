package dev.muffar.moneyfikasi.statistic.detail

import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.TransactionType

data class StatisticDetailState(
    val transactions : List<Transaction> = emptyList(),
    val type : TransactionType = TransactionType.INCOME
)
