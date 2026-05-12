package dev.muffar.moneyfikasi.statistic.main.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.component.tabs.IncomeExpenseTabs
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryStatistic
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.resource.R

@Composable
fun CategoryDistributionSection(
    categoryStatistics: Map<CategoryType, List<CategoryStatistic>>,
    onItemClick: (Category) -> Unit,
    onShowAllClick: () -> Unit
) {
    val pagerState = rememberPagerState { 2 }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        StatisticSectionLabel(
            label = stringResource(R.string.category_distribution),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        PrimaryCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                IncomeExpenseTabs(
                    pagerState = pagerState,
                    fillMaxSize = false,
                    tabPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp)
                ) { index ->
                    when (index) {
                        0 -> CategoryDistributionContent(
                            categoryStatistics = categoryStatistics[CategoryType.INCOME]
                                ?: emptyList(),
                            onItemClick = onItemClick,
                            onShowAllClick = onShowAllClick
                        )

                        1 -> CategoryDistributionContent(
                            categoryStatistics = categoryStatistics[CategoryType.EXPENSE]
                                ?: emptyList(),
                            onItemClick = onItemClick,
                            onShowAllClick = onShowAllClick
                        )
                    }
                }
            }
        }
    }
}
