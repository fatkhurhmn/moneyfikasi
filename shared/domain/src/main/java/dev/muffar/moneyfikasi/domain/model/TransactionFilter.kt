package dev.muffar.moneyfikasi.domain.model

import dev.muffar.moneyfikasi.domain.utils.TimePeriod

data class TransactionFilter(
    val timePeriod: TimePeriod = TimePeriod.MONTHLY,
    val dateRange: DateRange = DateRange(),
    val categories: Set<Category> = emptySet(),
    val wallets: Set<Wallet> = emptySet(),
)