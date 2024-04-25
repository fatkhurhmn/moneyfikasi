package dev.muffar.moneyfikasi.statistic.detail

import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.TransactionType

sealed class StatisticDetailEvent {
    data class OnInitState(val transactions: List<Transaction>, val type: TransactionType) : StatisticDetailEvent()
}