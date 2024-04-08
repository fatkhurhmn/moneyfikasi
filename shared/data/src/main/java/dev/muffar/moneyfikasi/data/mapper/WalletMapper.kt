package dev.muffar.moneyfikasi.data.mapper

import dev.muffar.moneyfikasi.data.db.entity.WalletEntity
import dev.muffar.moneyfikasi.domain.model.Wallet

fun WalletEntity.toModel(): Wallet {
    return Wallet(
        id = id,
        name = name,
        icon = icon,
        color = color,
        balance = balance,
        isActive = isActive
    )
}

fun Wallet.toEntity(): WalletEntity {
    return WalletEntity(
        id = id,
        name = name,
        icon = icon,
        color = color,
        balance = balance,
        isActive = isActive
    )
}

fun List<WalletEntity>.mapToModel(): List<Wallet> {
    return map { it.toModel() }
}

fun List<Wallet>.mapToEntity(): List<WalletEntity> {
    return map { it.toEntity() }
}