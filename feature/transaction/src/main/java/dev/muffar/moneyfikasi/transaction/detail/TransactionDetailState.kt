package dev.muffar.moneyfikasi.transaction.detail

import dev.muffar.moneyfikasi.domain.model.Debt
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.TransferDetail
import dev.muffar.moneyfikasi.domain.model.Wallet
import java.util.UUID

data class TransactionDetailState(
    val transactionId: UUID? = null,
    val transaction: Transaction? = null,
    val transferDetail: TransferDetail? = null,
    val linkedDebt: Debt? = null,
    val isDebtHistory: Boolean = false,
    val debtWallet: Wallet? = null,
    val showAlert: Boolean = false,
    val history: List<Transaction> = emptyList()
) {
    val isTransfer: Boolean
        get() = transaction == null

    val canShowPlus: Boolean
        get() = !isTransfer && linkedDebt != null && !isDebtHistory
}
