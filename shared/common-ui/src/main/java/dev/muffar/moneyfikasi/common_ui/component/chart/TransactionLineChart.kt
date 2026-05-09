package dev.muffar.moneyfikasi.common_ui.component.chart

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
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.chart.legend.LegendRow
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.format
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import java.util.Locale

@Composable
fun TransactionLineChart(
    modifier: Modifier = Modifier,
    incomeTransactions: List<Transaction>,
    expenseTransactions: List<Transaction>,
    dateRange: DateRange,
) {
    val financeColors = MoneyfikasiTheme.financeColors
    val incomeColor = financeColors.income.toArgb()
    val expenseColor = financeColors.expense.toArgb()
    val textColor = MaterialTheme.colorScheme.onSurfaceVariant.toArgb()
    val surfaceColor = MaterialTheme.colorScheme.surface.toArgb()

    val chartData = remember(incomeTransactions, expenseTransactions, dateRange) {
        prepareLineData(incomeTransactions, expenseTransactions, dateRange)
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
            LegendRow(modifier.align(Alignment.CenterHorizontally))
            ChartView(
                chartData = chartData,
                incomeColor = incomeColor,
                expenseColor = expenseColor,
                textColor = textColor,
                surfaceColor = surfaceColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
            )
        }
    }
}

fun prepareLineData(
    incomeTransactions: List<Transaction>,
    expenseTransactions: List<Transaction>,
    dateRange: DateRange,
): ChartData {
    val labels = mutableListOf<String>()
    val incomeValues = mutableListOf<Double>()
    val expenseValues = mutableListOf<Double>()
    val start =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(dateRange.start), ZoneId.systemDefault())

    when (dateRange.timePeriod) {
        TimePeriod.DAILY -> {
            val income = incomeTransactions.groupBy { it.date.hour }
            val expense = expenseTransactions.groupBy { it.date.hour }
            for (i in 0 until 24) {
                labels.add(String.format(Locale.getDefault(), "%02d:00", i))
                val i2 = income[i]?.sumOf { it.amount } ?: 0.0
                val e2 = expense[i]?.sumOf { it.amount } ?: 0.0
                incomeValues.add(i2)
                expenseValues.add(e2)
            }
        }

        TimePeriod.WEEKLY -> {
            val income = incomeTransactions.groupBy { it.date.toLocalDate() }
            val expense = expenseTransactions.groupBy { it.date.toLocalDate() }
            for (i in 0 until 7) {
                val day = start.plusDays(i.toLong())
                labels.add(day.format("dd MMM"))
                val i2 = income[day.toLocalDate()]?.sumOf { it.amount } ?: 0.0
                val e2 = expense[day.toLocalDate()]?.sumOf { it.amount } ?: 0.0
                incomeValues.add(i2)
                expenseValues.add(e2)
            }
        }

        TimePeriod.MONTHLY -> {
            val days = start.plusMonths(1).minusDays(1).dayOfMonth
            val income = incomeTransactions.groupBy { it.date.dayOfMonth }
            val expense = expenseTransactions.groupBy { it.date.dayOfMonth }
            for (i in 1..days) {
                labels.add(start.withDayOfMonth(i).format("dd MMM"))
                val i2 = income[i]?.sumOf { it.amount } ?: 0.0
                val e2 = expense[i]?.sumOf { it.amount } ?: 0.0
                incomeValues.add(i2)
                expenseValues.add(e2)
            }
        }

        TimePeriod.YEARLY -> {
            val income = incomeTransactions.groupBy { it.date.monthValue }
            val expense = expenseTransactions.groupBy { it.date.monthValue }
            for (i in 0 until 12) {
                val month = start.plusMonths(i.toLong())
                labels.add(month.format("dd MMM"))
                val i2 = income[month.monthValue]?.sumOf { it.amount } ?: 0.0
                val e2 = expense[month.monthValue]?.sumOf { it.amount } ?: 0.0
                incomeValues.add(i2)
                expenseValues.add(e2)
            }
        }

        else -> {
            labels.add("Total")
            val i2 = incomeTransactions.sumOf { it.amount }
            val e2 = expenseTransactions.sumOf { it.amount }
            incomeValues.add(i2)
            expenseValues.add(e2)

        }
    }

    return ChartData(labels, incomeValues, expenseValues)
}
