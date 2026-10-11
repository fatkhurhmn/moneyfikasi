package dev.muffar.moneyfikasi.data.mapper

import dev.muffar.moneyfikasi.data.db.entity.DebtEntity
import dev.muffar.moneyfikasi.domain.model.Debt
import dev.muffar.moneyfikasi.utils.constants.UUIDConst

fun DebtEntity.toDomain(): Debt {
    return Debt(
        id = this.id,
        name = this.name,
        type = this.type,
        totalAmount = this.totalAmount,
        paidAmount = this.paidAmount,
        dueDate = this.dueDate,
        note = this.note,
        walletId = this.walletId ?: UUIDConst.empty,
        transactionId = this.transactionId,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun Debt.toEntity(): DebtEntity {
    return DebtEntity(
        id = this.id,
        name = this.name,
        type = this.type,
        totalAmount = this.totalAmount,
        paidAmount = this.paidAmount,
        dueDate = this.dueDate,
        note = this.note,
        walletId = this.walletId,
        transactionId = this.transactionId,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}
