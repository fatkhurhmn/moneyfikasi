package dev.muffar.moneyfikasi.preset.add_edit.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.TransactionType
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
        val event = viewModel::onEvent

        val type = it.arguments?.getString(Screen.AddEditPreset.TYPE)?.let { value ->
            TransactionType.fromString(value)
        }

        LaunchedEffect(Unit) {
            event(AddEditPresetEvent.InitType(type ?: TransactionType.EXPENSE))
        }

        AddEditPresetScreen(
            state = state,
            eventFlow = viewModel.eventFlow,
            onNameChange = { event(AddEditPresetEvent.NameChanged(it)) },
            onAmountChange = { event(AddEditPresetEvent.AmountChanged(it)) },
            onCategoryChange = { event(AddEditPresetEvent.CategoryChanged(it)) },
            onAddNewCategoryClick = {},
            onAddNewWalletClick = {},
            onWalletChange = { event(AddEditPresetEvent.WalletChanged(it)) },
            onNoteChange = { event(AddEditPresetEvent.NoteChanged(it)) },
            onSaveClick = { event(AddEditPresetEvent.SavePreset) },
            onBackClick = navigateBack
        )
    }
}

fun NavController.toAddEditPresetScreen(type: TransactionType, id: UUID? = null) {
    navigate(Screen.AddEditPreset.routeWithArg(type, id))
}
