package dev.muffar.moneyfikasi.domain.usecase.category

import dev.muffar.moneyfikasi.domain.repository.CategoryRepository
import java.util.UUID

class UpdateCategory(
    private val categoryRepository: CategoryRepository,
) {

    suspend operator fun invoke(id: UUID, isActive: Boolean) {
        categoryRepository.updateCategory(id, isActive)
    }
} 