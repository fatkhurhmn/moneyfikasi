package dev.muffar.moneyfikasi.category.list.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.category.list.CategoriesScreen
import dev.muffar.moneyfikasi.category.list.CategoriesViewModel
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.navigation.Screen
import java.util.UUID

fun NavGraphBuilder.categoriesNavigation(
    navigateToAddCategory: (CategoryType) -> Unit,
    navigateToDetailCategory: (CategoryType, UUID) -> Unit,
    navigateBack: () -> Unit,
) {
    composable(route = Screen.Categories.route) {
        val viewModel = hiltViewModel<CategoriesViewModel>()
        val state = viewModel.state.value

        CategoriesScreen(
            state = state,
            onAddCategoryClick = navigateToAddCategory,
            onCategoryItemClick = navigateToDetailCategory,
            onBackClick = navigateBack
        )
    }
}

fun NavController.toCategoriesScreen() {
    navigate(Screen.Categories.route)
}