package dev.muffar.moneyfikasi.category.list

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryType

data class CategoriesState(
    val categories : List<Category> = emptyList(),
    val tabs : List<String> = CategoryType.entries.map { categoryType -> categoryType.name },
)
