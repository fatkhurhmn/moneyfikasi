package dev.muffar.moneyfikasi.debt.add_edit

import dev.muffar.moneyfikasi.domain.model.Debt
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.utils.extensions.StringExt.clearThousandFormat
import java.util.UUID

data class AddEditDebtState(
    val id: UUID? = null,
    val name: String = "",
    val nameError: ErrorMessage = ErrorMessage(),
    val totalAmount: String = "0",
    val totalAmountError: ErrorMessage = ErrorMessage(),
    val paidAmount: String = "0",
    val paidAmountError: ErrorMessage = ErrorMessage(),
    val dueDate: Long? = null,
    val note: String = "",
    val showAlert: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) {
    val debt: Debt
        get() = Debt(
            id = id ?: UUID.randomUUID(),
            name = name.trim(),
            totalAmount = totalAmount.clearThousandFormat().toDoubleOrNull() ?: 0.0,
            paidAmount = paidAmount.clearThousandFormat().toDoubleOrNull() ?: 0.0,
            dueDate = dueDate,
            note = note.trim(),
            createdAt = createdAt,
            updatedAt = System.currentTimeMillis()
        )
}
