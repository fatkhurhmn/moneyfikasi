package dev.muffar.moneyfikasi.domain.usecase.category

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow

class GetAllCategories(
    private val repository: CategoryRepository,
) {

    operator fun invoke(includeTransfer: Boolean = false): Flow<List<Category>> {
        return repository.getAllCategories(includeTransfer)
    }
} 