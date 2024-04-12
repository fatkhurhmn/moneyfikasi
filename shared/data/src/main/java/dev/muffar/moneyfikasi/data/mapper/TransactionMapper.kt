package dev.muffar.moneyfikasi.data.mapper

import dev.muffar.moneyfikasi.data.db.entity.TransactionEntity
import dev.muffar.moneyfikasi.data.db.entity.TransactionWithWalletAndCategory
import dev.muffar.moneyfikasi.domain.model.Transaction

fun TransactionWithWalletAndCategory.toModel(): Transaction {
    return Transaction(
        id = transaction.id,
        wallet = wallet.toModel(),
        category = category.toModel(),
        type = transaction.type,
        amount = transaction.amount,
        date = transaction.date,
        description = transaction.description
    )
}

fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = id,
        walletId = wallet.id,
        categoryId = category.id,
        type = type,
        amount = amount,
        date = date,
        description = description
    )
}

fun List<TransactionWithWalletAndCategory>.mapToModel(): List<Transaction> {
    return map { it.toModel() }
}

fun List<Transaction>.mapToEntity(): List<TransactionEntity> {
    return map { it.toEntity() }
}