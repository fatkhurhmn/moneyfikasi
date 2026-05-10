package dev.muffar.moneyfikasi.statistic.main.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.component.tabs.IncomeExpenseTabHeader
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
    var selectedTab by remember { mutableIntStateOf(0) }
    PrimaryCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            StatisticSectionLabel(
                label = stringResource(R.string.category_distribution),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 8.dp)
            )
            IncomeExpenseTabHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 2.dp),
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                labelStyle = MaterialTheme.typography.labelMedium.copy(fontSize = 14.sp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            when (selectedTab) {
                0 -> CategoryDistributionContent(
                    categoryStatistics = categoryStatistics[CategoryType.INCOME] ?: emptyList(),
                    onItemClick = onItemClick,
                    onShowAllClick = onShowAllClick
                )

                1 -> CategoryDistributionContent(
                    categoryStatistics = categoryStatistics[CategoryType.EXPENSE] ?: emptyList(),
                    onItemClick = onItemClick,
                    onShowAllClick = onShowAllClick
                )
            }
        }
    }
}
