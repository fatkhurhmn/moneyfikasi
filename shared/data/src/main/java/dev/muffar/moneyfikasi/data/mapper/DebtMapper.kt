package dev.muffar.moneyfikasi.data.mapper

import dev.muffar.moneyfikasi.data.db.entity.DebtEntity
import dev.muffar.moneyfikasi.domain.model.Debt

fun DebtEntity.toDomain(): Debt {
    return Debt(
        id = this.id,
        name = this.name,
        totalAmount = this.totalAmount,
        paidAmount = this.paidAmount,
        dueDate = this.dueDate,
        note = this.note,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun Debt.toEntity(): DebtEntity {
    return DebtEntity(
        id = this.id,
        name = this.name,
        totalAmount = this.totalAmount,
        paidAmount = this.paidAmount,
        dueDate = this.dueDate,
        note = this.note,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}
