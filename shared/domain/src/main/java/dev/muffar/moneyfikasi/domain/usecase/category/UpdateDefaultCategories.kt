package dev.muffar.moneyfikasi.domain.usecase.category

import dev.muffar.moneyfikasi.domain.model.AppLanguage
import dev.muffar.moneyfikasi.domain.repository.CategoryRepository

class UpdateDefaultCategories(
    private val repository: CategoryRepository,
) {

    suspend operator fun invoke(language: AppLanguage) {
        repository.updateDefaultCategories(language)
    }
}
