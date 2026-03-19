package dev.muffar.moneyfikasi.category.add_edit.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
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
        val state by viewModel.state.collectAsState()
        val event = viewModel::onEvent

        val type = it.arguments?.getString(Screen.AddEditCategory.TYPE)?.let { value ->
            CategoryType.fromString(value)
        }

        LaunchedEffect(Unit) {
            event(AddEditCategoryEvent.InitType(type ?: CategoryType.INCOME))
        }

        AddEditCategoryScreen(
            state = state,
            eventFlow = viewModel.eventFlow,
            onNameChange = { name ->
                event(AddEditCategoryEvent.NameChanged(name))
            },
            onIconSelect = { icon ->
                event(AddEditCategoryEvent.IconChanged(icon))
            },
            onColorSelect = { color ->
                event(AddEditCategoryEvent.ColorChanged(color))
            },
            onCategoryActive = { event(AddEditCategoryEvent.CategoryActivated) },
            onShowAlert = { showAlert ->
                event(AddEditCategoryEvent.ShowDeleteAlert(showAlert))
            },
            onSubmit = { event(AddEditCategoryEvent.SaveCategory) },
            onDelete = { event(AddEditCategoryEvent.DeleteCategory) },
            onBackClick = navigateBack
        )
    }
}

fun NavController.toAddEditCategoryScreen(type: CategoryType, id: UUID? = null) {
    navigate(Screen.AddEditCategory.routeWithArg(type, id))
}