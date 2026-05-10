package dev.muffar.moneyfikasi.common_ui.component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.Transaction
import java.text.DecimalFormat

@Composable
fun TransactionPieChart(
    transactions: Map<Category, List<Transaction>>,
) {
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    Crossfade(
        modifier = Modifier.padding(vertical = 8.dp),
        targetState = transactions,
        label = ""
    ) { pieChartData ->
        AndroidView(
            modifier = Modifier.size(150.dp),
            factory = { context ->
                PieChart(context).apply {
                    description.isEnabled = false
                    legend.isEnabled = false
                    isDrawHoleEnabled = true
                    setHoleColor(android.graphics.Color.TRANSPARENT)
                    setDrawCenterText(true)
                    setCenterTextSize(10f)
                    setDrawEntryLabels(false)
                    isRotationEnabled = false
                    animateX(500)
                    animateY(500)
                }
            },
            update = { pieChart ->
                val entries = ArrayList<PieEntry>()

                for (i in pieChartData.entries.indices) {
                    val item = pieChartData.entries.toList()[i]
                    val amount = item.value.sumOf { it.amount }
                    entries.add(PieEntry(amount.toFloat(), item.key.name))
                }

                val dataset = PieDataSet(entries, "").apply {
                    colors = pieChartData.map { Color(it.key.color).toArgb() }
                    setDrawValues(false)
                }

                val data = PieData(dataset)

                pieChart.setCenterTextColor(onSurfaceColor.toArgb())
                pieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                    override fun onValueSelected(e: Entry?, h: Highlight?) {
                        if (e is PieEntry) {
                            val df = DecimalFormat("###,###,##0.0")
                            val total = data.yValueSum
                            val percentage = (e.value / total) * 100
                            pieChart.centerText = "${e.label}\n${df.format(percentage)}%"
                        }
                    }

                    override fun onNothingSelected() {
                        pieChart.centerText = ""
                    }
                })

                pieChart.data = data
                pieChart.invalidate()
            }
        )
    }
}