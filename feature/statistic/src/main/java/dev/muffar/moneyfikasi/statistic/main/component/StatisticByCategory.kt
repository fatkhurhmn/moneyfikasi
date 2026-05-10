package dev.muffar.moneyfikasi.statistic.main.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.component.tabs.IncomeExpenseTabHeader
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryStatistic
import dev.muffar.moneyfikasi.domain.model.CategoryType

@Composable
fun StatisticByCategory(
    categoryStatistics: Map<CategoryType, List<CategoryStatistic>>,
    onItemClick: (Category) -> Unit
) {
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
            var selectedTab by remember { mutableIntStateOf(0) }
            IncomeExpenseTabHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                labelStyle = MaterialTheme.typography.labelMedium.copy(fontSize = 14.sp),
                innerPadding = PaddingValues(vertical = 6.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            when (selectedTab) {
                0 -> StatisticByCategoryContent(categoryStatistics[CategoryType.INCOME]?.take(3) ?: emptyList(), onItemClick, {})
                1 -> StatisticByCategoryContent(categoryStatistics[CategoryType.EXPENSE]?.take(3) ?: emptyList(), onItemClick, {})
            }
        }
    }
}
