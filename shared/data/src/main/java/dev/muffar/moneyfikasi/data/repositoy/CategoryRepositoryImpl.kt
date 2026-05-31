package dev.muffar.moneyfikasi.data.repositoy

import android.content.Context
import android.content.res.Configuration
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.muffar.moneyfikasi.data.db.dao.CategoryDao
import dev.muffar.moneyfikasi.data.db.entity.CategoryEntity
import dev.muffar.moneyfikasi.data.mapper.toDomain
import dev.muffar.moneyfikasi.data.mapper.toEntity
import dev.muffar.moneyfikasi.data.utils.InitDataSource
import dev.muffar.moneyfikasi.domain.model.AppLanguage
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
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

    override fun getCategoriesByType(
        type: CategoryType,
        includeTransfer: Boolean
    ): Flow<List<Category>> {
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

    override suspend fun updateDefaultCategories(language: AppLanguage) {
        val targetCategories = InitDataSource.getCategories(context.localized(language))
        val defaultCategories = supportedDefaultCategories()
        val defaultNamesByKey = defaultCategories
            .groupBy { it.defaultKey }
            .mapValues { (_, categories) -> categories.map { it.name }.toSet() }

        val categoriesToUpdate = categoryDao.getAllCategoriesOnce().mapNotNull { category ->
            val targetCategory = targetCategories.firstOrNull { it.defaultKey == category.defaultKey }
            val defaultNames = defaultNamesByKey[category.defaultKey].orEmpty()

            when {
                targetCategory == null -> null
                category.name !in defaultNames -> null
                category.name == targetCategory.name -> null
                else -> category.copy(name = targetCategory.name)
            }
        }

        if (categoriesToUpdate.isNotEmpty()) {
            categoryDao.upsertCategories(categoriesToUpdate)
        }
    }

    private fun supportedDefaultCategories(): List<CategoryEntity> {
        return listOf(
            AppLanguage.ENGLISH,
            AppLanguage.INDONESIAN,
        ).flatMap { language ->
            InitDataSource.getCategories(context.localized(language))
        }
    }

    private fun Context.localized(language: AppLanguage): Context {
        val locale = when (language) {
            AppLanguage.ENGLISH -> Locale.ENGLISH
            AppLanguage.INDONESIAN -> Locale.forLanguageTag("id")
            AppLanguage.SYSTEM -> Locale.getDefault()
        }
        val configuration = Configuration(resources.configuration).apply {
            setLocale(locale)
        }

        return createConfigurationContext(configuration)
    }

    private val CategoryEntity.defaultKey: DefaultCategoryKey
        get() = DefaultCategoryKey(
            icon = icon,
            color = color,
            type = type,
            isTransferCategory = isTransferCategory
        )

    private data class DefaultCategoryKey(
        val icon: String,
        val color: Long,
        val type: CategoryType,
        val isTransferCategory: Boolean,
    )
}
