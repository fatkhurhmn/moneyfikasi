package dev.muffar.moneyfikasi.statistic

import androidx.navigation.NavGraphBuilder
import dev.muffar.moneyfikasi.statistic.category_distribution.navigation.categoryDistributionNavigation
import dev.muffar.moneyfikasi.statistic.detail.navigation.statisticDetailNavigation
import dev.muffar.moneyfikasi.statistic.main.navigation.statisticNavigation
import java.util.UUID

fun NavGraphBuilder.statisticNavGraph(
    onNavigateToAllCategoryStatistic: (Long, Long) -> Unit,
    onNavigateToStatisticDetail: (Pair<Long, Long>, UUID, String) -> Unit,
    onNavigateToTransactionDetail: (UUID, Boolean) -> Unit,
    onNavigateBack: () -> Unit,
) {
    statisticNavigation(
        onNavigateToAllCategoryStatistic = onNavigateToAllCategoryStatistic,
        onNavigateToStatisticDetail = onNavigateToStatisticDetail,
    )

    categoryDistributionNavigation(
        onNavigateToStatisticDetail = onNavigateToStatisticDetail,
        onNavigateBack = onNavigateBack
    )

    statisticDetailNavigation(
        onNavigateToDetail = onNavigateToTransactionDetail,
        onNavigateBack = onNavigateBack
    )
}