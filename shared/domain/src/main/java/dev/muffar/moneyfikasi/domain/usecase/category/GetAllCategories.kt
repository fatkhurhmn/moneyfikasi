package dev.muffar.moneyfikasi.domain.usecase.category

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow

class GetAllCategories(
    private val categoryRepository: CategoryRepository,
) {

    suspend operator fun invoke(): Flow<List<Category>> {
        return categoryRepository.getAllCategories()
    }
} 