package dev.muffar.moneyfikasi.domain.usecase.recurring_transaction

import dev.muffar.moneyfikasi.domain.model.ProcessedRecurring
import dev.muffar.moneyfikasi.domain.model.RecurringEndType
import dev.muffar.moneyfikasi.domain.model.RecurringTransaction
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.repository.RecurringTransactionRepository
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.first
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneOffset

class ProcessRecurringTransactions(
    private val recurringTransactionRepository: RecurringTransactionRepository,
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(): List<ProcessedRecurring> {
        val recurringTransactions = recurringTransactionRepository.getAll().first()
        val today = LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
        val processedList = mutableListOf<ProcessedRecurring>()

        for (recurring in recurringTransactions) {
            if (!recurring.isActive) continue

            var currentRecurring = recurring
            var nextRun = currentRecurring.nextRun ?: currentRecurring.startDate

            while (nextRun <= today) {
                // Create transaction
                val transactionId = transactionRepository.addIncomeOrExpense(
                    amount = currentRecurring.amount,
                    type = currentRecurring.type,
                    date = Instant.ofEpochMilli(nextRun).atZone(ZoneOffset.UTC).toLocalDateTime(),
                    note = currentRecurring.name,
                    walletId = currentRecurring.wallet?.id ?: continue,
                    categoryId = currentRecurring.category?.id,
                    recurringTransactionId = currentRecurring.id
                )

                // Update next run
                val currentNextRunDate =
                    Instant.ofEpochMilli(nextRun).atZone(ZoneOffset.UTC).toLocalDateTime()
                val updatedNextRun = when (currentRecurring.frequency) {
                    TimePeriod.DAILY -> currentNextRunDate.plusDays(1)
                    TimePeriod.WEEKLY -> currentNextRunDate.plusWeeks(1)
                    TimePeriod.MONTHLY -> currentNextRunDate.plusMonths(1)
                    TimePeriod.YEARLY -> currentNextRunDate.plusYears(1)
                    else -> currentNextRunDate
                }.atZone(ZoneOffset.UTC).toInstant().toEpochMilli()

                // Check end condition after transaction created
                val isEndedNow = isCompleted(currentRecurring, updatedNextRun)

                processedList.add(
                    ProcessedRecurring(
                        name = currentRecurring.name,
                        amount = currentRecurring.amount,
                        transactionId = transactionId,
                        recurringId = currentRecurring.id,
                        type = currentRecurring.type,
                        isEnded = isEndedNow
                    )
                )

                currentRecurring = currentRecurring.copy(
                    lastRun = nextRun,
                    nextRun = updatedNextRun,
                    isActive = !isEndedNow
                )

                nextRun = updatedNextRun
                recurringTransactionRepository.save(currentRecurring)

                if (isEndedNow) break
            }
        }
        return processedList
    }

    private suspend fun isCompleted(recurring: RecurringTransaction, nextRun: Long): Boolean {
        return when (recurring.endType) {
            RecurringEndType.NEVER -> false
            RecurringEndType.ON_DATE -> recurring.endDate?.let { nextRun > it } ?: false
            RecurringEndType.AFTER_OCCURRENCES -> {
                val occurrences =
                    transactionRepository.getTransactionCountByRecurringId(recurring.id)
                recurring.occurrenceCount?.let { occurrences >= it } ?: false
            }
        }
    }
}