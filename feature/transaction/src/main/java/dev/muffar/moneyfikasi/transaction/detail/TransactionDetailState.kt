package dev.muffar.moneyfikasi.transaction.detail

import dev.muffar.moneyfikasi.domain.model.Debt
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.TransferDetail
import java.util.UUID

data class TransactionDetailState(
    val transactionId: UUID? = null,
    val transaction: Transaction? = null,
    val transferDetail: TransferDetail? = null,
    val linkedDebt: Debt? = null,
    val showAlert: Boolean = false,
    val showPlusSheet: Boolean = false,
    val plusTab: Int = 0,
    val plusAmount: String = "",
    val plusAmountError: ErrorMessage = ErrorMessage(),
    val isRecording: Boolean = false,
    val history: List<Transaction> = emptyList()
) {
    val isTransfer: Boolean
        get() = transaction == null

    val canShowPlus: Boolean
        get() = !isTransfer && linkedDebt != null

    val isPlusPaymentTab: Boolean
        get() {
            val debt = linkedDebt ?: return false
            return if (debt.type == DebtType.DEBT) {
                plusTab == 1
            } else {
                plusTab == 0
            }
        }
}
