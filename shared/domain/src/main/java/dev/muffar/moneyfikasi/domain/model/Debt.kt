package dev.muffar.moneyfikasi.domain.model

import java.util.UUID

data class Debt(
    val id: UUID = UUID.randomUUID(),
    val name: String = "",
    val totalAmount: Double = 0.0,
    val paidAmount: Double = 0.0,
    val dueDate: Long? = null,
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
) {
    val remainingAmount: Double
        get() = totalAmount - paidAmount
}
