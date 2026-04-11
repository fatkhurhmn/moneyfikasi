package dev.muffar.moneyfikasi.data.repositoy

import dev.muffar.moneyfikasi.data.db.dao.CategoryDao
import dev.muffar.moneyfikasi.data.mapper.toDomain
import dev.muffar.moneyfikasi.data.mapper.toEntity
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
) : CategoryRepository {

    override fun getAllCategories(excludeTransfer: Boolean): Flow<List<Category>> {
        return categoryDao.getAllCategories()
            .map { entities ->
                entities.filter { excludeTransfer || !it.isTransferCategory }.map { it.toDomain() }
            }
    }

    override suspend fun getCategoryById(id: UUID): Category? {
        return categoryDao.getCategoryById(id)?.toDomain()
    }

    override fun getCategoriesByType(type: CategoryType, includeTransfer: Boolean): Flow<List<Category>> {
        return categoryDao.getCategoriesByType(type).map { entities ->
            entities.filter { includeTransfer || !it.isTransferCategory }.map { it.toDomain() }
        }
    }

    override suspend fun upsertCategory(category: Category) {
        categoryDao.upsertCategory(category.toEntity())
    }

    override suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category.toEntity())
    }
}