package dev.muffar.moneyfikasi.domain.usecase.recurring_transaction

import dev.muffar.moneyfikasi.domain.model.ProcessedRecurring
import dev.muffar.moneyfikasi.domain.model.RecurringEndType
import dev.muffar.moneyfikasi.domain.model.RecurringTransaction
import dev.muffar.moneyfikasi.domain.repository.RecurringTransactionRepository
import dev.muffar.moneyfikasi.domain.repository.TransactionRepository
import dev.muffar.moneyfikasi.domain.utils.RecurringScheduleCalculator
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
                val updatedNextRun = RecurringScheduleCalculator.nextRunAfter(
                    nextRun,
                    currentRecurring.frequency
                )

                if (updatedNextRun == null) {
                    recurringTransactionRepository.save(currentRecurring.copy(isActive = false))
                    break
                }

                val walletId = currentRecurring.wallet?.id
                if (walletId == null) {
                    recurringTransactionRepository.save(currentRecurring.copy(isActive = false))
                    break
                }

                // Create transaction
                val transactionId = transactionRepository.addIncomeOrExpense(
                    amount = currentRecurring.amount,
                    type = currentRecurring.type,
                    date = Instant.ofEpochMilli(nextRun).atZone(ZoneOffset.UTC).toLocalDateTime(),
                    note = currentRecurring.name,
                    walletId = walletId,
                    categoryId = currentRecurring.category?.id,
                    recurringTransactionId = currentRecurring.id
                )

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
