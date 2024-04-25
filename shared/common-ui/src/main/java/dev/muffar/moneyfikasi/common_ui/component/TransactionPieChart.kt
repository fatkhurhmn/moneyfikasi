package dev.muffar.moneyfikasi.common_ui.component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import dev.muffar.moneyfikasi.domain.model.Transaction
import java.text.DecimalFormat

@Composable
fun TransactionPieChart(
    valueColor: Int,
    modifier: Modifier = Modifier,
    transactions: List<Transaction>,
) {
    val transactionByCategory = transactions.groupBy { it.category }
    Crossfade(
        targetState = transactionByCategory,
        label = ""
    ) { pieChartData ->
        AndroidView(
            modifier = modifier.size(250.dp),
            factory = { context ->
                PieChart(context).apply {
                    description.isEnabled = false
                    legend.isEnabled = false
                    isDrawHoleEnabled = false
                    setUsePercentValues(true)
                    animateX(500)
                    animateY(500)
                }
            },
            update = { pieChart ->
                val entries = ArrayList<PieEntry>()

                for (i in pieChartData.entries.indices) {
                    val item = pieChartData.entries.toList()[i]
                    val amount = item.value.sumOf { it.amount }
                    entries.add(PieEntry(amount.toFloat()))
                }

                val ds = PieDataSet(entries, "").apply {
                    colors = pieChartData.map { Color(it.key.color).toArgb() }
                    yValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
                    xValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
                    sliceSpace = 2f
                    valueTextSize = 14f
                    valueTextColor = valueColor
                }

                val d = PieData(ds)
                d.setValueFormatter(
                    object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            val df = DecimalFormat("###,###,##0.0")
                            df.isDecimalSeparatorAlwaysShown = false
                            return df.format(value) + "%"
                        }
                    }
                )

                pieChart.data = d
                pieChart.invalidate()
            }
        )
    }
}