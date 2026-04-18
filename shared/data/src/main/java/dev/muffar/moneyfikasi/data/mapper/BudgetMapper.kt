package dev.muffar.moneyfikasi.data.mapper

import dev.muffar.moneyfikasi.data.db.entity.BudgetEntity
import dev.muffar.moneyfikasi.data.db.entity.BudgetWithCategory
import dev.muffar.moneyfikasi.domain.model.AppIcon
import dev.muffar.moneyfikasi.domain.model.Budget
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryType

fun BudgetWithCategory.toDomain(): Budget {
    return Budget(
        id = this.budget.id,
        amount = this.budget.amount,
        category = this.category?.toDomain() ?: Category(
            name = "Unauthorized",
            icon = AppIcon.Widgets.name,
            color = 0xFFb8b4aa,
            type = CategoryType.EXPENSE,
        )
    )
}

fun Budget.toEntity(): BudgetEntity {
    return BudgetEntity(
        id = this.id,
        amount = this.amount,
        categoryId = this.category.id
    )
}
