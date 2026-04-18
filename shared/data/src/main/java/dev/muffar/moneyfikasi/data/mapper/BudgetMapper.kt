package dev.muffar.moneyfikasi.data.mapper

import dev.muffar.moneyfikasi.data.db.entity.BudgetEntity
import dev.muffar.moneyfikasi.data.db.entity.BudgetWithCategory
import dev.muffar.moneyfikasi.domain.model.Budget

fun BudgetWithCategory.toDomain(): Budget {
    return Budget(
        id = this.budget.id,
        name = this.budget.name,
        amount = this.budget.amount,
        category = this.category?.toDomain()
    )
}

fun Budget.toEntity(): BudgetEntity {
    return BudgetEntity(
        id = this.id,
        name = this.name,
        amount = this.amount,
        categoryId = this.category?.id
    )
}
