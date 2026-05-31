package dev.muffar.moneyfikasi.domain.repository

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.AppLanguage
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface CategoryRepository {
    fun getAllCategories(excludeTransfer: Boolean): Flow<List<Category>>

    suspend fun getCategoryById(id: UUID): Category?

    fun getCategoriesByType(type: CategoryType, includeTransfer: Boolean): Flow<List<Category>>

    suspend fun upsertCategory(category: Category)

    suspend fun deleteCategory(category: Category)

    suspend fun updateDefaultCategories(language: AppLanguage)
}
