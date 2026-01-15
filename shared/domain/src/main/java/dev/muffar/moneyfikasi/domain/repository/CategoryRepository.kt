package dev.muffar.moneyfikasi.domain.repository

import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryType
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface CategoryRepository {
    fun getAllCategories(): Flow<List<Category>>

    suspend fun getCategoryById(id: UUID): Category?

    fun getCategoriesByType(type: CategoryType): Flow<List<Category>>

    suspend fun upsertCategory(category: Category)

    suspend fun deleteCategory(category: Category)
}