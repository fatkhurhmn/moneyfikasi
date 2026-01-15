package dev.muffar.moneyfikasi.domain.usecase.category

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.repository.CategoryRepository
import java.util.UUID

class GetCategoryById(
    private val repository: CategoryRepository,
) {

    suspend operator fun invoke(id: UUID): Category? {
        return repository.getCategoryById(id)
    }
}