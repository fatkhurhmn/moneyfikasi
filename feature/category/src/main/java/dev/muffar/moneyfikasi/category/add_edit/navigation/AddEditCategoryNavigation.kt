package dev.muffar.moneyfikasi.category.add_edit.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.category.add_edit.AddEditCategoryEvent
import dev.muffar.moneyfikasi.category.add_edit.AddEditCategoryScreen
import dev.muffar.moneyfikasi.category.add_edit.AddEditCategoryViewModel
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.navigation.Screen
import java.util.UUID

fun NavGraphBuilder.addEditCategoryNavigation(
    navigateBack: () -> Unit,
) {
    composable(route = Screen.AddEditCategory.route) {
        val viewModel = hiltViewModel<AddEditCategoryViewModel>()
        val state = viewModel.state.value
        val event = viewModel::onEvent

        val type = it.arguments?.getString(Screen.AddEditCategory.TYPE)?.let { value ->
            CategoryType.fromString(value)
        }

        LaunchedEffect(Unit) {
            event(AddEditCategoryEvent.OnInitType(type))
        }

        AddEditCategoryScreen(
            state = state,
            eventFlow = viewModel.eventFlow,
            onNameChange = { name ->
                event(AddEditCategoryEvent.OnNameChange(name))
            },
            onIconChange = { icon ->
                event(AddEditCategoryEvent.OnIconChange(icon))
            },
            onColorChange = { color ->
                event(AddEditCategoryEvent.OnColorChange(color))
            },
            onShowBottomSheet = { sheetType ->
                event(AddEditCategoryEvent.OnBottomSheetChange(sheetType))
            },
            onIsActiveChange = { event(AddEditCategoryEvent.OnIsActiveChange) },
            onSubmit = { event(AddEditCategoryEvent.OnSubmitEditCategory) },
            onBackClick = navigateBack
        )
    }
}

fun NavController.toAddEditCategoryScreen(type: CategoryType, id: UUID? = null) {
    navigate(Screen.AddEditCategory.routeWithArg(type, id))
}