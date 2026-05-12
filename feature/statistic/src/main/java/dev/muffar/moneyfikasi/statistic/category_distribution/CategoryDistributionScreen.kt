package dev.muffar.moneyfikasi.statistic.category_distribution

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.component.pie_chart.CategoryDistributionChart
import dev.muffar.moneyfikasi.common_ui.component.statistic.CategoryDistributionItem
import dev.muffar.moneyfikasi.common_ui.component.tabs.IncomeExpenseTabs
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
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        val pagerState = rememberPagerState { 2 }

        IncomeExpenseTabs(
            modifier = modifier.padding(innerPadding),
            fillMaxSize = true,
            pagerState = pagerState,
        ) { index ->
            val categoryType = if (index == 0) CategoryType.INCOME else CategoryType.EXPENSE
            val categoryStatistics = state.categoryStatistics[categoryType] ?: emptyList()
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CategoryDistributionChart(
                            categoryStatistics = categoryStatistics,
                            size = 200.dp,
                        )
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
        }
    }
}
