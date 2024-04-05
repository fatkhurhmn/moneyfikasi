package dev.muffar.moneyfikasi.domain.usecase.category

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.repository.CategoryRepository

class SaveAllCategories(
    private val categoryRepository: CategoryRepository,
) {

    suspend operator fun invoke(categories: List<Category>) {
        categoryRepository.saveAllCategories(categories)
    }
}