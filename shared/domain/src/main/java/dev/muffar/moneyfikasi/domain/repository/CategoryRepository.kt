package dev.muffar.moneyfikasi.domain.repository

import dev.muffar.moneyfikasi.domain.model.Category
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface CategoryRepository {
    suspend fun saveCategory(category: Category)
    suspend fun saveAllCategories(categories: List<Category>)
    suspend fun updateCategory(id: UUID, isActive: Boolean)
    suspend fun deleteCategory(id: UUID)
    suspend fun deleteAllCategories()
    suspend fun getAllCategories(isActive: Boolean): Flow<List<Category>>
    suspend fun getCategoryById(id: UUID): Category?
}