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
    navigateToAddWallet: () -> Unit,
    navigateToAddCategory: (type: CategoryType) -> Unit,
    navigateBack: () -> Unit
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
            event(AddEditPresetEvent.TypeChanged(type ?: TransactionType.EXPENSE, true))
        }

        AddEditPresetScreen(
            state = state,
            eventFlow = viewModel.eventFlow,
            onTypeChange = { type -> event(AddEditPresetEvent.TypeChanged(type, false)) },
            onNameChange = { name -> event(AddEditPresetEvent.NameChanged(name)) },
            onAmountChange = { amount -> event(AddEditPresetEvent.AmountChanged(amount)) },
            onCategoryChange = { category -> event(AddEditPresetEvent.CategoryChanged(category)) },
            onAddNewCategoryClick = { navigateToAddCategory(state.categoryType) },
            onAddNewWalletClick = navigateToAddWallet,
            onWalletChange = { wallet -> event(AddEditPresetEvent.WalletChanged(wallet)) },
            onDescriptionChange = { description ->
                event(AddEditPresetEvent.DescriptionChanged(description))
            },
            onSaveClick = { event(AddEditPresetEvent.SavePreset) },
            onBackClick = navigateBack,
            onDeleteClick = { event(AddEditPresetEvent.DeletePreset) },
            onShowDeleteAlert = { show -> event(AddEditPresetEvent.ShowDeleteAlert(show)) }
        )
    }
}

fun NavController.toAddEditPresetScreen(type: TransactionType, id: UUID? = null) {
    navigate(Screen.AddEditPreset.routeWithArg(type, id))
}
