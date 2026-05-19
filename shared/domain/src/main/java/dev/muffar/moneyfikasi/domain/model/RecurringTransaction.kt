package dev.muffar.moneyfikasi.domain.model

import java.util.UUID

data class RecurringTransaction(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val amount: Double,
    val type: TransactionType,
    val category: Category? = null,
    val wallet: Wallet? = null,
    val note: String? = null,
    val frequency: TimePeriod = TimePeriod.MONTHLY,
    val startDate: Long,
    val lastRun: Long? = null,
    val nextRun: Long? = null,
    val isActive: Boolean = true
)
