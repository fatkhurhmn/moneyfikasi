package dev.muffar.moneyfikasi.category

import androidx.navigation.NavGraphBuilder
import dev.muffar.moneyfikasi.category.add_edit.navigation.addEditCategoryNavigation
import dev.muffar.moneyfikasi.category.list.navigation.categoriesNavigation
import dev.muffar.moneyfikasi.domain.model.CategoryType
import java.util.UUID

fun NavGraphBuilder.categoriesNavGraph(
    navigateToAddCategory: (CategoryType) -> Unit,
    navigateToEditCategory: (CategoryType, UUID) -> Unit,
    navigateBack: () -> Unit,
) {
    categoriesNavigation(
        navigateToAddCategory = navigateToAddCategory,
        navigateToEditCategory = navigateToEditCategory,
        navigateBack = navigateBack,
    )
    addEditCategoryNavigation(
        navigateBack = navigateBack,
    )
}