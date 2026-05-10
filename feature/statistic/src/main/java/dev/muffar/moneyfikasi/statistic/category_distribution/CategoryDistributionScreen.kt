package dev.muffar.moneyfikasi.statistic.category_distribution

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.component.tabs.IncomeExpenseTabHeader
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.statistic.main.component.CategoryDistributionItem
import java.util.UUID

@Composable
fun CategoryDistributionScreen(
    modifier: Modifier = Modifier,
    state: CategoryDistributionState,
    onBackClick: () -> Unit,
    onItemClick: (UUID, String) -> Unit,
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val categoryType = if (selectedTab == 0) CategoryType.INCOME else CategoryType.EXPENSE
    val categoryStatistics = state.categoryStatistics[categoryType] ?: emptyList()

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.category_distribution),
                onBackClick = onBackClick
            )
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        ) {
            IncomeExpenseTabHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                labelStyle = MaterialTheme.typography.labelMedium.copy(fontSize = 14.sp),
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
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
