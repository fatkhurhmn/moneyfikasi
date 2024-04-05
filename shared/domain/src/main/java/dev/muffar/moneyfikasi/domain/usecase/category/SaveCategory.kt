package dev.muffar.moneyfikasi.domain.usecase.category

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.repository.CategoryRepository

class SaveCategory(
    private val categoryRepository: CategoryRepository,
) {

    suspend operator fun invoke(category: Category) {
        categoryRepository.saveCategory(category)
    }
}