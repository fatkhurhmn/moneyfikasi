package dev.muffar.moneyfikasi.domain.usecase.category

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow

class GetCategoryByType(
    private val repository: CategoryRepository,
) {

    operator fun invoke(
        type: CategoryType,
        includeTransfer: Boolean = false
    ): Flow<List<Category>> {
        return repository.getCategoriesByType(type, includeTransfer)
    }
}