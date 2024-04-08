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
import java.util.UUID

fun NavGraphBuilder.addCategoryNavigation(
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
            eventFlow = viewModel.eventFlow,
            onNameChange = { name ->
                event(AddCategoryEvent.OnNameChange(name))
            },
            onIconChange = { icon ->
                event(AddCategoryEvent.OnIconChange(icon))
            },
            onColorChange = { color ->
                event(AddCategoryEvent.OnColorChange(color))
            },
            onShowBottomSheet = { sheetType ->
                event(AddCategoryEvent.OnBottomSheetChange(sheetType))
            },
            onIsActiveChange = { event(AddCategoryEvent.OnIsActiveChange) },
            onSubmit = { event(AddCategoryEvent.OnSubmitCategory) },
            onBackClick = navigateBack
        )
    }
}

fun NavController.toAddCategoryScreen(type: CategoryType, id: UUID? = null) {
    navigate(Screen.AddCategory.routeWithArg(type, id))
}