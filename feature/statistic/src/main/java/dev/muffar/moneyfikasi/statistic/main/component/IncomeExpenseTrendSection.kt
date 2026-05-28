package dev.muffar.moneyfikasi.statistic.main.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.component.line_chart.ChartData
import dev.muffar.moneyfikasi.common_ui.component.line_chart.IncomeExpenseChart
import dev.muffar.moneyfikasi.common_ui.component.line_chart.legend.LegendRow
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.model.TransactionTrend
import dev.muffar.moneyfikasi.resource.R

@Composable
fun IncomeExpenseTrendSection(
    modifier: Modifier = Modifier,
    trend: TransactionTrend,
    timePeriod: TimePeriod,
    onSliding: (Boolean) -> Unit = {},
) {
    val financeColors = MoneyfikasiTheme.financeColors
    val incomeColor = financeColors.income.toArgb()
    val expenseColor = financeColors.expense.toArgb()
    val textColor = MaterialTheme.colorScheme.onSurfaceVariant.toArgb()
    val surfaceColor = MaterialTheme.colorScheme.surface.toArgb()

    var selectedTrendType by remember { mutableStateOf(TrendGraphType.BOTH) }

    val chartData = remember(trend) {
        ChartData(
            labels = trend.labels,
            incomeValues = trend.incomeValues,
            expenseValues = trend.expenseValues
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatisticSectionLabel(
                label = stringResource(R.string.trend),
            )
            TrendGraphToggle(
                selectedType = selectedTrendType,
                onTypeSelected = { selectedTrendType = it },
            )
        }
        PrimaryCard {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                LegendRow(
                    modifier = Modifier.align(Alignment.End),
                    showIncome = selectedTrendType != TrendGraphType.EXPENSE,
                    showExpense = selectedTrendType != TrendGraphType.INCOME,
                )
                if (timePeriod == TimePeriod.ALL || timePeriod == TimePeriod.CUSTOM) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.unavailable),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                } else {
                    IncomeExpenseChart(
                        chartData = chartData,
                        incomeColor = incomeColor,
                        expenseColor = expenseColor,
                        textColor = textColor,
                        surfaceColor = surfaceColor,
                        showIncome = selectedTrendType != TrendGraphType.EXPENSE,
                        showExpense = selectedTrendType != TrendGraphType.INCOME,
                        onSliding = onSliding,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                    )
                }
            }
        }
    }
}
