package dev.muffar.moneyfikasi.recurring_transaction.add_edit

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.domain.model.RecurringEndType
import dev.muffar.moneyfikasi.domain.model.RecurringTransaction
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.utils.extensions.StringExt.clearThousandFormat
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneOffset
import java.util.UUID

data class AddEditRecurringTransactionState(
    val id: UUID? = null,
    val name: String = "",
    val amount: String = "0",
    val type: TransactionType = TransactionType.EXPENSE,
    val category: Category? = null,
    val wallet: Wallet? = null,
    val frequency: TimePeriod = TimePeriod.MONTHLY,
    val startDate: Long = LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli(),
    val endType: RecurringEndType = RecurringEndType.NEVER,
    val endDate: Long = LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli(),
    val occurrenceCount: String = "1",
    val isSkipFirst: Boolean = false,
    val isActive: Boolean = true,
    val nameError: ErrorMessage = ErrorMessage(),
    val amountError: ErrorMessage = ErrorMessage(),
    val categoryError: ErrorMessage = ErrorMessage(),
    val walletError: ErrorMessage = ErrorMessage(),
    val occurrenceCountError: ErrorMessage = ErrorMessage(),
    val categories: List<Category> = emptyList(),
    val wallets: List<Wallet> = emptyList(),
    val isLoading: Boolean = false,
    val lastRun: Long? = null,
    val nextRun: Long? = null,
    val initialStartDate: Long? = null,
) {
    val recurringTransaction: RecurringTransaction
        get() {
            val calculatedNextRun = if (id == null || startDate != initialStartDate) {
                if (isSkipFirst) {
                    val startDateTime =
                        Instant.ofEpochMilli(startDate).atZone(ZoneOffset.UTC).toLocalDateTime()
                    when (frequency) {
                        TimePeriod.DAILY -> startDateTime.plusDays(1)
                        TimePeriod.WEEKLY -> startDateTime.plusWeeks(1)
                        TimePeriod.MONTHLY -> startDateTime.plusMonths(1)
                        TimePeriod.YEARLY -> startDateTime.plusYears(1)
                        else -> startDateTime
                    }.atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
                } else {
                    startDate
                }
            } else {
                nextRun ?: startDate
            }

            return RecurringTransaction(
                id = id ?: UUID.randomUUID(),
                name = name.trim(),
                amount = amount.clearThousandFormat().toDoubleOrNull() ?: 0.0,
                type = type,
                category = category,
                wallet = wallet,
                frequency = frequency,
                startDate = startDate,
                endType = endType,
                endDate = if (endType == RecurringEndType.ON_DATE) endDate else null,
                occurrenceCount = if (endType == RecurringEndType.AFTER_OCCURRENCES) occurrenceCount.toIntOrNull() else null,
                lastRun = if (startDate != initialStartDate) null else lastRun,
                nextRun = calculatedNextRun,
                isActive = isActive
            )
        }
}
