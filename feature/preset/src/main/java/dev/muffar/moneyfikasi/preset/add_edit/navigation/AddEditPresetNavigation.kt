package dev.muffar.moneyfikasi.preset.add_edit.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.preset.add_edit.AddEditPresetEvent
import dev.muffar.moneyfikasi.preset.add_edit.AddEditPresetScreen
import dev.muffar.moneyfikasi.preset.add_edit.AddEditPresetViewModel
import java.util.UUID

fun NavGraphBuilder.addEditPresetNavigation(
    navigateBack: () -> Unit,
) {
    composable(
        route = Screen.AddEditPreset.route,
        arguments = listOf(
            navArgument(Screen.AddEditPreset.PRESET_ID) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    ) {
        val viewModel = hiltViewModel<AddEditPresetViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        AddEditPresetScreen(
            state = state,
            eventFlow = viewModel.eventFlow,
            onNameChange = { viewModel.onEvent(AddEditPresetEvent.NameChanged(it)) },
            onAmountChange = { viewModel.onEvent(AddEditPresetEvent.AmountChanged(it)) },
            onCategoryChange = { viewModel.onEvent(AddEditPresetEvent.CategoryChanged(it)) },
            onAddNewCategoryClick = {},
            onAddNewWalletClick = {},
            onWalletChange = { viewModel.onEvent(AddEditPresetEvent.WalletChanged(it)) },
            onNoteChange = { viewModel.onEvent(AddEditPresetEvent.NoteChanged(it)) },
            onSaveClick = { viewModel.onEvent(AddEditPresetEvent.SavePreset) },
            onBackClick = navigateBack
        )
    }
}

fun NavController.toAddEditPresetScreen(id: UUID? = null) {
    navigate(Screen.AddEditPreset.routeWithArg(id))
}
