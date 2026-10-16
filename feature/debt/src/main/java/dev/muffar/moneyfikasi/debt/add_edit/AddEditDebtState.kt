package dev.muffar.moneyfikasi.debt.add_edit

import dev.muffar.moneyfikasi.domain.model.Debt
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.toMilliseconds
import dev.muffar.moneyfikasi.utils.extensions.StringExt.clearThousandFormat
import org.threeten.bp.LocalDateTime
import java.util.UUID

data class AddEditDebtState(
    val id: UUID? = null,
    val name: String = "",
    val nameError: ErrorMessage = ErrorMessage(),
    val type: DebtType = DebtType.DEBT,
    val totalAmount: String = "0",
    val totalAmountError: ErrorMessage = ErrorMessage(),
    val paidAmount: String = "0",
    val paidAmountError: ErrorMessage = ErrorMessage(),
    val dueDate: Long? = LocalDateTime.now().plusMonths(1).toMilliseconds(),
    val dueDateError: ErrorMessage = ErrorMessage(),
    val note: String = "",
    val wallet: Wallet = Wallet(),
    val walletError: ErrorMessage = ErrorMessage(),
    val walletOptions: List<Wallet> = emptyList(),
    val transactionId: UUID? = null,
    val createdAt: Long = System.currentTimeMillis()
) {
    val debt: Debt
        get() = Debt(
            id = id ?: UUID.randomUUID(),
            name = name.trim(),
            type = type,
            totalAmount = totalAmount.clearThousandFormat().toDoubleOrNull() ?: 0.0,
            paidAmount = paidAmount.clearThousandFormat().toDoubleOrNull() ?: 0.0,
            dueDate = dueDate,
            note = note.trim(),
            walletId = wallet.id,
            transactionId = transactionId,
            createdAt = createdAt,
            updatedAt = System.currentTimeMillis()
        )
}
