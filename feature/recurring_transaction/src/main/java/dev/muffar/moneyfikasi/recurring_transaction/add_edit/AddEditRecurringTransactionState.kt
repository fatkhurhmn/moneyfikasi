package dev.muffar.moneyfikasi.recurring_transaction.add_edit

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.domain.model.RecurringEndType
import dev.muffar.moneyfikasi.domain.model.RecurringTransaction
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.utils.extensions.StringExt.clearThousandFormat
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
    val isActive: Boolean = true,
    val nameError: ErrorMessage = ErrorMessage(),
    val amountError: ErrorMessage = ErrorMessage(),
    val categoryError: ErrorMessage = ErrorMessage(),
    val walletError: ErrorMessage = ErrorMessage(),
    val categories: List<Category> = emptyList(),
    val wallets: List<Wallet> = emptyList(),
    val isLoading: Boolean = false,
) {
    val recurringTransaction: RecurringTransaction
        get() = RecurringTransaction(
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
            isActive = isActive
        )
}
