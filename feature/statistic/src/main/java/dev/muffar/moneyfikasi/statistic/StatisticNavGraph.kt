package dev.muffar.moneyfikasi.statistic

import androidx.navigation.NavGraphBuilder
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.statistic.detail.navigation.statisticDetailNavigation
import dev.muffar.moneyfikasi.statistic.main.navigation.statisticNavigation

fun NavGraphBuilder.statisticNavGraph(
    transactions: List<Transaction>,
    onNavigateToStatisticDetail: (List<Transaction>) -> Unit,
    onNavigateBack: () -> Unit,
) {
    statisticNavigation(
        onNavigateToStatisticDetail = onNavigateToStatisticDetail,
    )

    statisticDetailNavigation(
        transactions = transactions,
        onNavigateBack = onNavigateBack
    )
}