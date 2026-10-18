package dev.muffar.moneyfikasi.transaction.detail.plus

import dev.muffar.moneyfikasi.domain.model.Debt
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.domain.model.Wallet
import java.util.UUID

data class DebtPlusState(
    val debtId: UUID? = null,
    val debt: Debt? = null,
    val editingHistoryId: UUID? = null,
    val tab: Int = 0,
    val amount: String = "",
    val amountError: ErrorMessage = ErrorMessage(),
    val wallet: Wallet = Wallet(),
    val walletError: ErrorMessage = ErrorMessage(),
    val walletOptions: List<Wallet> = emptyList(),
    val date: Long = System.currentTimeMillis(),
    val isRecording: Boolean = false
) {
    val isPaymentTab: Boolean
        get() {
            val type = debt?.type ?: return false
            return if (type == DebtType.DEBT) {
                tab == 1
            } else {
                tab == 0
            }
        }
}
