package dev.muffar.moneyfikasi.statistic.category_distribution

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.EmptyDataList
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.component.pie_chart.CategoryDistributionChart
import dev.muffar.moneyfikasi.common_ui.component.statistic.CategoryDistributionItem
import dev.muffar.moneyfikasi.common_ui.component.tabs.IncomeExpenseTabs
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.resource.R
import java.util.UUID

@Composable
fun CategoryDistributionScreen(
    modifier: Modifier = Modifier,
    state: CategoryDistributionState,
    onBackClick: () -> Unit,
    onItemClick: (UUID, String) -> Unit,
) {
    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.category_distribution),
                onBackClick = onBackClick
            )
        },
    ) { innerPadding ->
        val pagerState = rememberPagerState { 2 }

        IncomeExpenseTabs(
            modifier = modifier.padding(innerPadding),
            fillMaxSize = true,
            pagerState = pagerState,
        ) { index ->
            val categoryType = if (index == 0) CategoryType.INCOME else CategoryType.EXPENSE
            val categoryStatistics = state.categoryStatistics[categoryType] ?: emptyList()
            if (categoryStatistics.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        PrimaryCard {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CategoryDistributionChart(
                                    categoryStatistics = categoryStatistics,
                                    categoryType = categoryType
                                )
                            }
                        }
                    }
                    items(categoryStatistics, key = { it.category.id }) { stat ->
                        CategoryDistributionItem(
                            category = stat.category,
                            amount = stat.amount,
                            percentage = stat.percentage,
                            quantity = stat.transactionCount,
                            onClick = { category -> onItemClick(category.id, category.name) }
                        )
                    }
                }
            } else {
                EmptyDataList(
                    title = stringResource(R.string.no_transactions),
                    description = stringResource(R.string.no_transactions_message)
                )
            }
        }
    }
}
