package dev.muffar.moneyfikasi.recurring_transaction.add_edit

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.domain.model.RecurringEndType
import dev.muffar.moneyfikasi.domain.model.RecurringTransaction
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.utils.RecurringScheduleCalculator
import dev.muffar.moneyfikasi.utils.constants.UUIDConst
import dev.muffar.moneyfikasi.utils.extensions.StringExt.clearThousandFormat
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import java.util.UUID

data class AddEditRecurringTransactionState(
    val id: UUID? = null,
    val name: String = "",
    val amount: String = "0",
    val type: TransactionType = TransactionType.EXPENSE,
    val category: Category = Category(),
    val wallet: Wallet = Wallet(),
    val frequency: TimePeriod = TimePeriod.MONTHLY,
    val startDate: Long = LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli(),
    val startTime: Pair<Int, Int> = 0 to 0,
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
    val initialFrequency: TimePeriod? = null,
    val initialEndType: RecurringEndType? = null,
    val initialEndDate: Long? = null,
    val initialOccurrenceCount: Int? = null,
) {
    val recurringTransaction: RecurringTransaction
        get() {
            val currentEndDate = if (endType == RecurringEndType.ON_DATE) endDate else null
            val currentOccurrenceCount =
                if (endType == RecurringEndType.AFTER_OCCURRENCES) {
                    occurrenceCount.toIntOrNull()
                } else {
                    null
                }
            val isScheduleChanged =
                id == null ||
                    startDate != initialStartDate ||
                    frequency != initialFrequency ||
                    endType != initialEndType ||
                    currentEndDate != initialEndDate ||
                    currentOccurrenceCount != initialOccurrenceCount

            val startDateTime = LocalDateTime.of(
                Instant.ofEpochMilli(startDate).atZone(ZoneId.systemDefault()).toLocalDate(),
                LocalTime.of(startTime.first, startTime.second)
            ).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

            val calculatedNextRun = when {
                id == null -> RecurringScheduleCalculator.initialNextRun(
                    startDate = startDateTime,
                    frequency = frequency,
                    skipFirstRun = isSkipFirst
                )

                isScheduleChanged -> {
                    val firstRun = RecurringScheduleCalculator.initialNextRun(
                        startDate = startDateTime,
                        frequency = frequency,
                        skipFirstRun = isSkipFirst
                    )
                    val today = LocalDateTime.now(ZoneId.systemDefault()).atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
                    RecurringScheduleCalculator.nextRunOnOrAfter(
                        startDate = firstRun,
                        frequency = frequency,
                        targetDate = today
                    )
                }

                else -> nextRun ?: startDateTime
            }

            return RecurringTransaction(
                id = id ?: UUID.randomUUID(),
                name = name.trim(),
                amount = amount.clearThousandFormat().toDoubleOrNull() ?: 0.0,
                type = type,
                category = if (category.id == UUIDConst.empty) null else category,
                wallet = if (wallet.id == UUIDConst.empty) null else wallet,
                frequency = frequency,
                startDate = startDateTime,
                endType = endType,
                endDate = currentEndDate,
                occurrenceCount = currentOccurrenceCount,
                lastRun = if (startDate != initialStartDate) null else lastRun,
                nextRun = calculatedNextRun,
                isActive = if (isScheduleChanged) true else isActive
            )
        }
}
