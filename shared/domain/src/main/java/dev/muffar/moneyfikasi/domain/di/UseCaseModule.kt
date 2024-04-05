package dev.muffar.moneyfikasi.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.muffar.moneyfikasi.domain.repository.CategoryRepository
import dev.muffar.moneyfikasi.domain.usecase.category.CategoryUseCases
import dev.muffar.moneyfikasi.domain.usecase.category.DeleteAllCategories
import dev.muffar.moneyfikasi.domain.usecase.category.DeleteCategory
import dev.muffar.moneyfikasi.domain.usecase.category.GetAllCategories
import dev.muffar.moneyfikasi.domain.usecase.category.GetCategoryById
import dev.muffar.moneyfikasi.domain.usecase.category.SaveAllCategories
import dev.muffar.moneyfikasi.domain.usecase.category.SaveCategory
import dev.muffar.moneyfikasi.domain.usecase.category.*

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideCategoryUseCases(
        categoryRepository: CategoryRepository,
    ) = CategoryUseCases(
        saveCategory = SaveCategory(categoryRepository),
        saveAllCategories = SaveAllCategories(categoryRepository),
        updateCategory = UpdateCategory(categoryRepository),
        deleteCategory = DeleteCategory(categoryRepository),
        deleteAllCategories = DeleteAllCategories(categoryRepository),
        getAllCategories = GetAllCategories(categoryRepository),
        getCategoryById = GetCategoryById(categoryRepository),
    )

    @Provides
    fun provideGetAllCategories(
        categoryRepository: CategoryRepository,
    ) = GetAllCategories(categoryRepository)
}