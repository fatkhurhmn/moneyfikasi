package dev.muffar.moneyfikasi.transaction.detail

import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.TransferDetail
import java.util.UUID

data class TransactionDetailState(
    val transactionId: UUID? = null,
    val transaction: Transaction? = null,
    val transferDetail: TransferDetail? = null,
    val showAlert: Boolean = false
)