package dev.muffar.moneyfikasi.domain.usecase.category

import dev.muffar.moneyfikasi.domain.repository.CategoryRepository

class DeleteAllCategories(
    private val categoryRepository: CategoryRepository,
) {

    suspend operator fun invoke() {
        categoryRepository.deleteAllCategories()
    }
}