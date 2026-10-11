package dev.muffar.moneyfikasi.domain.model

import dev.muffar.moneyfikasi.utils.constants.UUIDConst
import java.util.UUID

data class Debt(
    val id: UUID = UUID.randomUUID(),
    val name: String = "",
    val type: DebtType = DebtType.DEBT,
    val totalAmount: Double = 0.0,
    val paidAmount: Double = 0.0,
    val dueDate: Long? = null,
    val note: String = "",
    val walletId: UUID = UUIDConst.empty,
    val transactionId: UUID? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
) {
    val remainingAmount: Double
        get() = totalAmount - paidAmount
}
