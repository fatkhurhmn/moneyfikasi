package dev.muffar.moneyfikasi.export.navigation

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.export.ExportScreen
import dev.muffar.moneyfikasi.export.ExportViewModel
import dev.muffar.moneyfikasi.navigation.Screen
import kotlinx.coroutines.launch

fun NavController.toExportScreen() {
    navigate(Screen.Export.route)
}

fun NavGraphBuilder.exportNavGraph(
    context: Context,
    onBackClick: () -> Unit,
) {
    composable(route = Screen.Export.route) {
        val viewModel = hiltViewModel<ExportViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val scope = rememberCoroutineScope()

        ExportScreen(
            state = state,
            eventFlow = viewModel.eventFlow,
            onEvent = viewModel::onEvent,
            onExportTransactions = { uri ->
                scope.launch {
                    context.contentResolver.openOutputStream(uri)?.use {
                        viewModel.exportTransactions(it)
                    }
                }
            },
            onBackClick = onBackClick
        )
    }
}
