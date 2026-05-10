package dev.muffar.moneyfikasi.statistic.category_distribution.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.statistic.category_distribution.CategoryDistributionScreen
import dev.muffar.moneyfikasi.statistic.category_distribution.CategoryDistributionViewModel
import java.util.UUID

fun NavGraphBuilder.categoryDistributionNavigation(
    onNavigateToStatisticDetail: (Pair<Long, Long>, UUID, String) -> Unit,
    onNavigateBack: () -> Unit,
) {
    composable(
        route = Screen.CategoryDistribution.route,
        arguments = listOf(
            navArgument(Screen.CategoryDistribution.START_DATE) { type = NavType.LongType },
            navArgument(Screen.CategoryDistribution.END_DATE) { type = NavType.LongType }
        )
    ) {
        val viewModel = hiltViewModel<CategoryDistributionViewModel>()
        val state by viewModel.state.collectAsState()
        val startDate = it.arguments?.getLong(Screen.CategoryDistribution.START_DATE) ?: 0L
        val endDate = it.arguments?.getLong(Screen.CategoryDistribution.END_DATE) ?: 0L

        CategoryDistributionScreen(
            state = state,
            onBackClick = onNavigateBack,
            onItemClick = { id, name ->
                onNavigateToStatisticDetail(startDate to endDate, id, name)
            }
        )
    }
}

fun NavController.toCategoryDistributionScreen(startDate: Long, endDate: Long) {
    navigate(Screen.CategoryDistribution.routeWithArg(startDate, endDate))
}
