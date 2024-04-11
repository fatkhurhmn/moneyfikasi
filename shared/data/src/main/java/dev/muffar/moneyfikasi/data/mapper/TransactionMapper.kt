package dev.muffar.moneyfikasi.data.mapper

import dev.muffar.moneyfikasi.data.db.entity.TransactionEntity
import dev.muffar.moneyfikasi.domain.model.Transaction

fun TransactionEntity.toModel(): Transaction {
    return Transaction(
        id = id,
        walletId = walletId,
        categoryId = categoryId,
        type = type,
        amount = amount,
        date = date,
        description = description
    )
}

fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = id,
        walletId = walletId,
        categoryId = categoryId,
        type = type,
        amount = amount,
        date = date,
        description = description
    )
}

fun List<TransactionEntity>.mapToModel(): List<Transaction> {
    return map { it.toModel() }
}

fun List<Transaction>.mapToEntity(): List<TransactionEntity> {
    return map { it.toEntity() }
}