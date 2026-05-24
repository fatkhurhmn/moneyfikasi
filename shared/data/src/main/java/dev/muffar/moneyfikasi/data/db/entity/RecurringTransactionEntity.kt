package dev.muffar.moneyfikasi.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.muffar.moneyfikasi.domain.model.RecurringEndType
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.model.TransactionType
import java.util.UUID

@Entity(tableName = "recurring_transactions")
data class RecurringTransactionEntity(
    @PrimaryKey val id: UUID,
    val name: String,
    val amount: Double,
    val type: TransactionType,
    val categoryId: UUID?,
    val walletId: UUID?,
    val frequency: TimePeriod,
    val startDate: Long,
    val endType: RecurringEndType,
    val endDate: Long?,
    val occurrenceCount: Int?,
    val lastRun: Long?,
    val nextRun: Long?,
    val isActive: Boolean
)
