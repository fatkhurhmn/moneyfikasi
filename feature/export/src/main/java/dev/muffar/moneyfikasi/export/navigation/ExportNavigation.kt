package dev.muffar.moneyfikasi.export.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.export.ExportEvent
import dev.muffar.moneyfikasi.export.ExportScreen
import dev.muffar.moneyfikasi.export.ExportViewModel
import dev.muffar.moneyfikasi.navigation.Screen
import kotlinx.coroutines.launch

fun NavGraphBuilder.exportNavGraph(
    navigateBack: () -> Unit,
) {
    composable(route = Screen.Export.route) {
        val viewModel = hiltViewModel<ExportViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val onEvent = viewModel::onEvent
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        ExportScreen(
            state = state,
            eventFlow = viewModel.eventFlow,
            onStartDateChanged = { onEvent(ExportEvent.OnStartDateChanged(it)) },
            onEndDateChanged = { onEvent(ExportEvent.OnEndDateChanged(it)) },
            onFormatChanged = { onEvent(ExportEvent.OnFormatChanged(it)) },
            onExportTransactions = { uri ->
                scope.launch {
                    context.contentResolver.openOutputStream(uri)?.use {
                        viewModel.exportTransactions(it)
                    }
                }
            },
            onBackClick = navigateBack
        )
    }
}

fun NavController.toExportScreen() {
    navigate(Screen.Export.route)
}
