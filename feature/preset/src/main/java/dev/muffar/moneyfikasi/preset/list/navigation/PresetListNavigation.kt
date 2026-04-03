package dev.muffar.moneyfikasi.preset.list.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.preset.list.PresetListScreen
import dev.muffar.moneyfikasi.preset.list.PresetListViewModel
import java.util.UUID

fun NavGraphBuilder.presetListNavigation(
    navigateToAddPreset: (TransactionType) -> Unit,
    navigateToEditPreset: (TransactionType, UUID) -> Unit,
    navigateBack: () -> Unit,
) {
    composable(route = Screen.PresetList.route) {
        val viewModel = hiltViewModel<PresetListViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        PresetListScreen(
            state = state,
            onAddPresetClick = navigateToAddPreset,
            onPresetClick = navigateToEditPreset,
            onBackClick = navigateBack
        )
    }
}

fun NavController.toPresetListScreen() {
    navigate(Screen.PresetList.route)
}
