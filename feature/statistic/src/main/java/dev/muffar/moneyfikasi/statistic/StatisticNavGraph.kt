package dev.muffar.moneyfikasi.statistic

import androidx.navigation.NavGraphBuilder
import dev.muffar.moneyfikasi.statistic.detail.navigation.statisticDetailNavigation
import dev.muffar.moneyfikasi.statistic.main.navigation.statisticNavigation
import java.util.UUID

fun NavGraphBuilder.statisticNavGraph(
    onNavigateToStatisticDetail: (Pair<Long, Long>, UUID) -> Unit,
    onNavigateToTransactionDetail: (UUID) -> Unit,
    onNavigateBack: () -> Unit,
) {
    statisticNavigation(
        onNavigateToStatisticDetail = onNavigateToStatisticDetail,
    )

    statisticDetailNavigation(
        onNavigateToDetail = onNavigateToTransactionDetail,
        onNavigateBack = onNavigateBack
    )
}