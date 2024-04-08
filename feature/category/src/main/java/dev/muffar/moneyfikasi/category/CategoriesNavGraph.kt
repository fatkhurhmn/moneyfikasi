package dev.muffar.moneyfikasi.category

import androidx.navigation.NavGraphBuilder
import dev.muffar.moneyfikasi.category.add.navigation.addCategoryNavigation
import dev.muffar.moneyfikasi.category.list.navigation.categoriesNavigation
import dev.muffar.moneyfikasi.domain.model.CategoryType
import java.util.UUID

fun NavGraphBuilder.categoriesNavGraph(
    navigateToAddCategory: (CategoryType) -> Unit,
    navigateToDetailCategory: (CategoryType, UUID) -> Unit,
    navigateBack: () -> Unit,
) {
    categoriesNavigation(
        navigateToAddCategory = navigateToAddCategory,
        navigateToDetailCategory = navigateToDetailCategory,
        navigateBack = navigateBack,
    )
    addCategoryNavigation(
        navigateBack = navigateBack,
    )
}