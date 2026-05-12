package dev.muffar.moneyfikasi.common_ui.component.pie_chart

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import dev.muffar.moneyfikasi.domain.model.CategoryStatistic
import java.text.DecimalFormat

@Composable
fun CategoryDistributionChart(
    categoryStatistics: List<CategoryStatistic>,
    size: Dp = 150.dp
) {
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    Crossfade(
        modifier = Modifier.padding(vertical = 8.dp),
        targetState = categoryStatistics,
        label = ""
    ) { pieChartData ->
        AndroidView(
            modifier = Modifier.size(size),
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

                for (stat in pieChartData) {
                    entries.add(PieEntry(stat.amount.toFloat(), stat.category.name))
                }

                val dataset = PieDataSet(entries, "").apply {
                    colors = pieChartData.map { Color(it.category.color).toArgb() }
                    setDrawValues(false)
                    sliceSpace = 1f
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
