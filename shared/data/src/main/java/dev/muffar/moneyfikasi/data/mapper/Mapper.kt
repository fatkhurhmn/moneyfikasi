package dev.muffar.moneyfikasi.data.mapper

import dev.muffar.moneyfikasi.data.db.entity.CategoryEntity
import dev.muffar.moneyfikasi.data.db.entity.CategoryStatisticEntity
import dev.muffar.moneyfikasi.data.db.entity.PresetEntity
import dev.muffar.moneyfikasi.data.db.entity.TransactionTrendEntity
import dev.muffar.moneyfikasi.data.db.entity.PresetWithDetails
import dev.muffar.moneyfikasi.data.db.entity.TransactionEntity
import dev.muffar.moneyfikasi.data.db.entity.TransactionWithDetails
import dev.muffar.moneyfikasi.data.db.entity.WalletEntity
import dev.muffar.moneyfikasi.domain.model.AppIcon
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryStatistic
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.TransactionTrendItem
import dev.muffar.moneyfikasi.utils.extensions.LongExt.toLocalDateTime
import dev.muffar.moneyfikasi.domain.model.Preset
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.Wallet

// --- Wallet Mappers ---

fun WalletEntity.toDomain(): Wallet {
    return Wallet(
        id = this.id,
        name = this.name,
        icon = this.icon,
        color = this.color,
        balance = this.balance,
        isActive = this.isActive
    )
}

fun Wallet.toEntity(): WalletEntity {
    return WalletEntity(
        id = this.id,
        name = this.name,
        icon = this.icon,
        color = this.color,
        balance = this.balance,
        isActive = this.isActive
    )
}

// --- Category Mappers ---

fun CategoryEntity.toDomain(): Category {
    return Category(
        id = this.id,
        name = this.name,
        icon = this.icon,
        color = this.color,
        type = this.type,
        isActive = this.isActive
    )
}

fun CategoryStatisticEntity.toDomain(): CategoryStatistic {
    return CategoryStatistic(
        category = this.category.toDomain(),
        amount = this.totalAmount,
        transactionCount = this.transactionCount,
        percentage = 0.0
    )
}

fun TransactionTrendEntity.toDomain(): TransactionTrendItem {
    return TransactionTrendItem(
        date = this.date.toLocalDateTime(),
        amount = this.amount,
        type = this.type
    )
}

fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = this.id,
        name = this.name,
        icon = this.icon,
        color = this.color,
        type = this.type,
        isActive = this.isActive
    )
}

// --- Transaction Mappers ---

fun TransactionWithDetails.toDomain(): Transaction {
    return Transaction(
        id = this.transaction.id,
        amount = this.transaction.amount,
        date = this.transaction.date,
        note = this.transaction.note,
        type = this.transaction.type,
        wallet = this.wallet.toDomain(),
        category = this.category?.toDomain() ?: Category(
            name = "Unauthorized",
            icon = AppIcon.Widgets.name,
            color = 0xFFb8b4aa,
            type = CategoryType.EXPENSE,
        )
    )
}

fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = this.id,
        walletId = this.wallet.id,
        categoryId = this.category.id,
        type = this.type,
        amount = this.amount,
        date = this.date,
        note = this.note
    )
}

// --- Preset Mappers ---

fun PresetWithDetails.toDomain(): Preset {
    return Preset(
        id = this.preset.id,
        name = this.preset.name,
        amount = this.preset.amount,
        type = this.preset.type,
        wallet = this.wallet?.toDomain(),
        category = this.category?.toDomain(),
        description = this.preset.description
    )
}

fun Preset.toEntity(): PresetEntity {
    return PresetEntity(
        id = this.id,
        name = this.name,
        amount = this.amount,
        type = this.type,
        walletId = this.wallet?.id,
        categoryId = this.category?.id,
        description = this.description
    )
}