package dev.muffar.moneyfikasi.domain.usecase.category

data class CategoryUseCases(
    val saveCategory: SaveCategory,
    val saveAllCategories: SaveAllCategories,
    val updateCategory: UpdateCategory,
    val deleteCategory: DeleteCategory,
    val deleteAllCategories: DeleteAllCategories,
    val getAllCategories: GetAllCategories,
    val getCategoryById: GetCategoryById,
)
