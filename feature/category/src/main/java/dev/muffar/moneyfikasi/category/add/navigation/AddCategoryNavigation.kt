package dev.muffar.moneyfikasi.category.add.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.category.add.AddCategoryEvent
import dev.muffar.moneyfikasi.category.add.AddCategoryScreen
import dev.muffar.moneyfikasi.category.add.AddCategoryViewModel
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.navigation.Screen

fun NavGraphBuilder.addCategoryNavGraph(
    navigateBack: () -> Unit,
) {
    composable(route = Screen.AddCategory.route) {
        val viewModel = hiltViewModel<AddCategoryViewModel>()
        val state = viewModel.state.value
        val event = viewModel::onEvent

        val type = it.arguments?.getString(Screen.AddCategory.TYPE)?.let { value ->
            CategoryType.fromString(value)
        }

        LaunchedEffect(Unit) {
            event(AddCategoryEvent.OnInitType(type))
        }

        AddCategoryScreen(
            state = state,
            onBackClick = navigateBack
        )
    }
}

fun NavController.toAddCategoryScreen(type: CategoryType) {
    navigate(Screen.AddCategory.routeWithArg(type))
}