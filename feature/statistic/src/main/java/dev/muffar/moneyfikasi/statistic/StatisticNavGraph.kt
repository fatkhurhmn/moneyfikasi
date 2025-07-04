package dev.muffar.moneyfikasi.statistic

import androidx.navigation.NavGraphBuilder
import dev.muffar.moneyfikasi.statistic.detail.navigation.statisticDetailNavigation
import dev.muffar.moneyfikasi.statistic.main.navigation.statisticNavigation
import java.util.UUID

fun NavGraphBuilder.statisticNavGraph(
    dateRange: Pair<Long, Long>?,
    category: UUID?,
    onNavigateToStatisticDetail: (Pair<Long, Long>, UUID) -> Unit,
    onNavigateToTransactionDetail: (UUID) -> Unit,
    onNavigateBack: () -> Unit,
) {
    statisticNavigation(
        onNavigateToStatisticDetail = onNavigateToStatisticDetail,
    )

    statisticDetailNavigation(
        dateRange = dateRange,
        categoryId = category,
        onNavigateToDetail = onNavigateToTransactionDetail,
        onNavigateBack = onNavigateBack
    )
}