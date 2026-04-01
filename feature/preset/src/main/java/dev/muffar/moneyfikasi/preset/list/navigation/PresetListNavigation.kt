package dev.muffar.moneyfikasi.preset.list.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.preset.list.PresetListScreen
import dev.muffar.moneyfikasi.preset.list.PresetListViewModel

fun NavGraphBuilder.presetListNavigation(
    navigateBack: () -> Unit,
) {
    composable(route = Screen.PresetList.route) {
        val viewModel = hiltViewModel<PresetListViewModel>()
        val state by viewModel.state.collectAsState()

        PresetListScreen(
            state = state,
            onBackClick = navigateBack
        )
    }
}

fun NavController.toPresetListScreen() {
    navigate(Screen.PresetList.route)
}
