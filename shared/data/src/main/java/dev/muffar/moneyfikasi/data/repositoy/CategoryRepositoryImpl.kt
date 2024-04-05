package dev.muffar.moneyfikasi.data.repositoy

import dev.muffar.moneyfikasi.data.db.dao.CategoryDao
import dev.muffar.moneyfikasi.data.mapper.mapToEntity
import dev.muffar.moneyfikasi.data.mapper.mapToModel
import dev.muffar.moneyfikasi.data.mapper.toEntity
import dev.muffar.moneyfikasi.data.mapper.toModel
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject


class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
) : CategoryRepository {
    override suspend fun saveCategory(category: Category) {
        categoryDao.save(category.toEntity())
    }

    override suspend fun saveAllCategories(categories: List<Category>) {
        categoryDao.saveAll(categories.mapToEntity())
    }

    override suspend fun updateCategory(id: UUID, isActive: Boolean) {
        categoryDao.update(id, isActive)
    }

    override suspend fun deleteCategory(id: UUID) {
        categoryDao.delete(id)
    }

    override suspend fun deleteAllCategories() {
        categoryDao.deleteAll()
    }

    override suspend fun getAllCategories(isActive: Boolean): Flow<List<Category>> {
        return categoryDao.getAll(isActive).map {
            it.mapToModel()
        }
    }

    override suspend fun getCategoryById(id: UUID): Category? {
        return categoryDao.getById(id)?.toModel()
    }
}