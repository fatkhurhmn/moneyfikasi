package dev.muffar.moneyfikasi.category.add

import dev.muffar.moneyfikasi.domain.model.CategoryType

data class AddCategoryState(
    val type: CategoryType? = CategoryType.INCOME,
)