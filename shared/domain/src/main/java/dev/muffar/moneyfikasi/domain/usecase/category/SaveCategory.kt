package dev.muffar.moneyfikasi.domain.usecase.category

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.InvalidCategoryException
import dev.muffar.moneyfikasi.domain.repository.CategoryRepository

class SaveCategory(
    private val categoryRepository: CategoryRepository,
) {

    @Throws(InvalidCategoryException::class)
    suspend operator fun invoke(category: Category) {
        if (category.name.isEmpty()){
            throw InvalidCategoryException("Name cannot be empty")
        }

        if (category.icon.isEmpty()){
            throw InvalidCategoryException("Select an icon")
        }

        if (category.color == 0L){
            throw InvalidCategoryException("Select a color")
        }

        categoryRepository.saveCategory(category)
    }
}