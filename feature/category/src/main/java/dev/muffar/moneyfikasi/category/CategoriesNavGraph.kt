package dev.muffar.moneyfikasi.category

import androidx.navigation.NavGraphBuilder
import dev.muffar.moneyfikasi.category.add.navigation.addCategoryNavigation
import dev.muffar.moneyfikasi.category.list.navigation.categoriesNavigation
import dev.muffar.moneyfikasi.domain.model.CategoryType

fun NavGraphBuilder.categoriesNavGraph(
    navigateToAddCategory: (CategoryType) -> Unit,
    navigateBack: () -> Unit,
) {
    categoriesNavigation(
        navigateToAddCategory = navigateToAddCategory,
        navigateBack = navigateBack,
    )
    addCategoryNavigation(
        navigateBack = navigateBack,
    )
}