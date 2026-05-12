package dev.muffar.moneyfikasi.statistic.main.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.domain.model.StatisticInsight
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand

@Composable
fun StatisticInsightsSection(
    modifier: Modifier = Modifier,
    insight: StatisticInsight
) {
    val financeColors = MoneyfikasiTheme.financeColors

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        StatisticSectionLabel(
            label = stringResource(R.string.insights),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InsightItem(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.highest_expense),
                value = insight.highestExpense?.amount?.formatThousand() ?: "-",
                label = insight.highestExpense?.category?.name ?: "No Data",
                color = financeColors.expense
            )
            InsightItem(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.frequent_expense),
                value = insight.mostFrequentExpenseCategory?.category?.name ?: "-",
                label = "${insight.mostFrequentExpenseCategory?.transactionCount ?: 0} times",
                color = financeColors.expense
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InsightItem(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.highest_income),
                value = insight.highestIncome?.amount?.formatThousand() ?: "-",
                label = insight.highestIncome?.category?.name ?: "No Data",
                color = financeColors.income
            )
            InsightItem(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.frequent_income),
                value = insight.mostFrequentIncomeCategory?.category?.name ?: "-",
                label = "${insight.mostFrequentIncomeCategory?.transactionCount ?: 0} times",
                color = financeColors.income
            )
        }
    }
}