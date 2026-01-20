package dev.muffar.moneyfikasi.transaction.detail

import dev.muffar.moneyfikasi.common_ui.component.Message
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.TransferDetail
import java.util.UUID

data class TransactionDetailState(
    val transactionId: UUID? = null,
    val transaction: Transaction? = null,
    val transferDetail: TransferDetail? = null,
    val showAlert: Boolean = false,
    val message: Message = Message(),
    val messageVisibility: Boolean = false
) {
    val isTransfer: Boolean
        get() = transaction == null
}