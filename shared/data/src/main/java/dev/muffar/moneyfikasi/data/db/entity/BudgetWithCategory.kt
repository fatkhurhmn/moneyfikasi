package dev.muffar.moneyfikasi.data.db.entity

import androidx.room.Embedded
import androidx.room.Relation

data class BudgetWithCategory(
    @Embedded val budget: BudgetEntity,

    @Relation(
        parentColumn = "category_id",
        entityColumn = "id"
    )
    val category: CategoryEntity?
)
