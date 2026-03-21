package dev.muffar.moneyfikasi.search.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.search.SearchEvent
import dev.muffar.moneyfikasi.search.SearchScreen
import dev.muffar.moneyfikasi.search.SearchViewModel
import java.util.UUID

fun NavGraphBuilder.searchNavigation(
    onNavigateToTransactionDetail: (UUID, Boolean) -> Unit
) {
    composable(Screen.Search.route) {
        val viewModel = hiltViewModel<SearchViewModel>()
        val state by viewModel.state.collectAsState()
        val event = viewModel::onEvent

        SearchScreen(
            state = state,
            onQueryChange = { event.invoke(SearchEvent.QueryChanged(it)) },
            onNavigateToTransactionDetail = onNavigateToTransactionDetail
        )
    }
}

fun NavController.toSearchScreen() {
    navigate(Screen.Search.route)
}