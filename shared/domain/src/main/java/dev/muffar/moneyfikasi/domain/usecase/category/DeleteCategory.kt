package dev.muffar.moneyfikasi.domain.usecase.category

import dev.muffar.moneyfikasi.domain.repository.CategoryRepository
import java.util.UUID

class DeleteCategory(
    private val categoryRepository: CategoryRepository,
) {

    suspend operator fun invoke(id: UUID) {
        categoryRepository.deleteCategory(id)
    }
}