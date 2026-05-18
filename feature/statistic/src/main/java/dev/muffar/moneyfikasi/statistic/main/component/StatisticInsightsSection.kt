package dev.muffar.moneyfikasi.statistic.main.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.TrendingDown
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.domain.model.CategoryStatistic
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand

@Composable
fun StatisticInsightsSection(
    modifier: Modifier = Modifier,
    highestExpense: Transaction?,
    highestIncome: Transaction?,
    mostFreqExpenseCategory: CategoryStatistic?,
    mostFreqIncomeCategory: CategoryStatistic?,
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
                title = stringResource(R.string.highest_income),
                value = highestIncome?.amount?.formatThousand() ?: "-",
                label = highestIncome?.category?.name ?: "No Data",
                color = financeColors.income,
                iconName = Icons.AutoMirrored.Rounded.TrendingUp.name,
                iconColor = financeColors.income.toArgb().toLong(),
            )
            InsightItem(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.highest_expense),
                value = highestExpense?.amount?.formatThousand() ?: "-",
                label = highestExpense?.category?.name ?: "No Data",
                color = financeColors.expense,
                iconName = Icons.AutoMirrored.Rounded.TrendingDown.name,
                iconColor = financeColors.expense.toArgb().toLong(),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InsightItem(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.frequent_income),
                value = mostFreqIncomeCategory?.category?.name ?: "-",
                label = "${mostFreqIncomeCategory?.transactionCount ?: 0} times",
                color = financeColors.income,
                iconName = mostFreqIncomeCategory?.category?.icon,
                iconColor = mostFreqIncomeCategory?.category?.color,
            )
            InsightItem(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.frequent_expense),
                value = mostFreqExpenseCategory?.category?.name ?: "-",
                label = "${mostFreqExpenseCategory?.transactionCount ?: 0} times",
                color = financeColors.expense,
                iconName = mostFreqExpenseCategory?.category?.icon,
                iconColor = mostFreqExpenseCategory?.category?.color,
            )
        }
    }
}