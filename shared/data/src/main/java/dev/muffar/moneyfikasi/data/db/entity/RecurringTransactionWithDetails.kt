package dev.muffar.moneyfikasi.data.db.entity

import androidx.room.Embedded
import androidx.room.Relation

data class RecurringTransactionWithDetails(
    @Embedded val recurringTransaction: RecurringTransactionEntity,

    @Relation(
        parentColumn = "walletId",
        entityColumn = "id"
    )
    val wallet: WalletEntity?,

    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: CategoryEntity?
)
