package dev.muffar.moneyfikasi.statistic.main.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.EmptyDataList
import dev.muffar.moneyfikasi.common_ui.component.pie_chart.CategoryDistributionChart
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryStatistic
import dev.muffar.moneyfikasi.resource.R

@Composable
fun StatisticByCategoryContent(
    categoryStatistics: List<CategoryStatistic>,
    onItemClick: (Category) -> Unit,
    onShowAllClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (categoryStatistics.isNotEmpty()) {
            CategoryDistributionChart(categoryStatistics)
            Column {
                categoryStatistics.forEach { stat ->
                    StatisticByCategoryItem(
                        category = stat.category,
                        amount = stat.amount,
                        percentage = stat.percentage,
                        quantity = stat.transactionCount,
                        onClick = onItemClick
                    )
                }
            }

            Text(
                text = stringResource(R.string.see_all),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .clickable(onClick = onShowAllClick)
                    .padding(vertical = 2.dp, horizontal = 4.dp)
            )
        } else {
            EmptyDataList(
                painter = painterResource(R.drawable.ic_empty_transactions),
                title = stringResource(R.string.no_transactions),
                description = stringResource(R.string.no_transactions_message)
            )
        }
    }
}
