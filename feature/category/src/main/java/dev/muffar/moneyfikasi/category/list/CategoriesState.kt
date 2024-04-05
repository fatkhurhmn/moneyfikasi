package dev.muffar.moneyfikasi.category.list

import dev.muffar.moneyfikasi.domain.model.Category

data class CategoriesState(
    val categories : List<Category> = emptyList(),
)
