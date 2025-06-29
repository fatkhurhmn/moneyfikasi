package dev.muffar.moneyfikasi.statistic

import androidx.navigation.NavGraphBuilder
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.statistic.detail.navigation.statisticDetailNavigation
import dev.muffar.moneyfikasi.statistic.main.navigation.statisticNavigation
import java.util.UUID

fun NavGraphBuilder.statisticNavGraph(
    transactions: List<Transaction>,
    onNavigateToStatisticDetail: (List<Transaction>) -> Unit,
    onNavigateToTransactionDetail: (UUID) -> Unit,
    onNavigateBack: () -> Unit,
) {
    statisticNavigation(
        onNavigateToStatisticDetail = onNavigateToStatisticDetail,
    )

    statisticDetailNavigation(
        transactions = transactions,
        onNavigateToDetail = onNavigateToTransactionDetail,
        onNavigateBack = onNavigateBack
    )
}