package dev.muffar.moneyfikasi.data.db.entity

import androidx.room.Embedded
import androidx.room.Relation

data class TransactionWithWalletAndCategory (
    @Embedded
    val transaction: TransactionEntity,

    @Relation(
        parentColumn = "wallet_id",
        entityColumn = "id"
    )
    val wallet: WalletEntity,

    @Relation(
        parentColumn = "category_id",
        entityColumn = "id"
    )
    val category: CategoryEntity
)