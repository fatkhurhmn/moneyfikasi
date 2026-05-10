package dev.muffar.moneyfikasi.statistic.main.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import dev.muffar.moneyfikasi.domain.model.TransactionTrend
import dev.muffar.moneyfikasi.resource.R

@Composable
fun IncomeExpenseTrendSection(
    modifier: Modifier = Modifier,
    trend: TransactionTrend,
    onSliding: (Boolean) -> Unit = {},
) {
    val financeColors = MoneyfikasiTheme.financeColors
    val incomeColor = financeColors.income.toArgb()
    val expenseColor = financeColors.expense.toArgb()
    val textColor = MaterialTheme.colorScheme.onSurfaceVariant.toArgb()
    val surfaceColor = MaterialTheme.colorScheme.surface.toArgb()

    val chartData = remember(trend) {
        ChartData(
            labels = trend.labels,
            incomeValues = trend.incomeValues,
            expenseValues = trend.expenseValues
        )
    }

    PrimaryCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            StatisticSectionLabel(
                label = stringResource(R.string.trend),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            LegendRow(modifier.align(Alignment.End))
            IncomeExpenseChart(
                chartData = chartData,
                incomeColor = incomeColor,
                expenseColor = expenseColor,
                textColor = textColor,
                surfaceColor = surfaceColor,
                onSliding = onSliding,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
            )
        }
    }
}
