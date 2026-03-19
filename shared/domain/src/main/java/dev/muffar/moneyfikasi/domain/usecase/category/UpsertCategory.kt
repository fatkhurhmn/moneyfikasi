package dev.muffar.moneyfikasi.domain.usecase.category

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.repository.CategoryRepository

class UpsertCategory(
    private val repository: CategoryRepository,
) {

    suspend operator fun invoke(category: Category) {
        repository.upsertCategory(category)
    }
}