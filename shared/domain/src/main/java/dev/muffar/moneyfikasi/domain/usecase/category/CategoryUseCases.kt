package dev.muffar.moneyfikasi.domain.usecase.category

data class CategoryUseCases(
    val upsertCategory: UpsertCategory,
    val deleteCategory: DeleteCategory,
    val getAllCategories: GetAllCategories,
    val getCategoryById: GetCategoryById,
    val getCategoryByType: GetCategoryByType
)
