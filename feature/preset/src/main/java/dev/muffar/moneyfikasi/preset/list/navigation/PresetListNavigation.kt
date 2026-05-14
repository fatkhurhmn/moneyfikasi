package dev.muffar.moneyfikasi.preset.list.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.preset.list.PresetsScreen
import dev.muffar.moneyfikasi.preset.list.PresetsViewModel
import java.util.UUID

fun NavGraphBuilder.presetsNavigation(
    navigateToAddPreset: (TransactionType) -> Unit,
    navigateToEditPreset: (TransactionType, UUID) -> Unit,
    navigateBack: () -> Unit,
) {
    composable(route = Screen.Presets.route) {
        val viewModel = hiltViewModel<PresetsViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        PresetsScreen(
            state = state,
            onAddPresetClick = navigateToAddPreset,
            onPresetClick = navigateToEditPreset,
            onBackClick = navigateBack
        )
    }
}

fun NavController.toPresetsScreen() {
    navigate(Screen.Presets.route)
}
