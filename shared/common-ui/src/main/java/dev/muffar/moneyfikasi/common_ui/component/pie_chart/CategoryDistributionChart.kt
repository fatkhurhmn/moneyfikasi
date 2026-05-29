package dev.muffar.moneyfikasi.common_ui.component.pie_chart

import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
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
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.domain.model.CategoryStatistic
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand
import java.text.DecimalFormat

@Composable
fun CategoryDistributionChart(
    categoryStatistics: List<CategoryStatistic>,
    modifier: Modifier = Modifier,
    size: Dp = 150.dp,
    categoryType: CategoryType
) {
    val title = if (categoryType == CategoryType.INCOME) {
        stringResource(R.string.label_total_income)
    } else {
        stringResource(R.string.label_total_expense)
    }

    val color = if (categoryType == CategoryType.INCOME) {
        MoneyfikasiTheme.financeColors.income
    } else {
        MoneyfikasiTheme.financeColors.expense
    }.toArgb()

    val centerTitleColor = MaterialTheme.colorScheme.onSurfaceVariant.toArgb()
    val totalAmount = categoryStatistics.sumOf { it.amount }

    fun createCenterText(
        title: String,
        value: String,
    ): SpannableString {
        val s = SpannableString("$title\n$value")

        val titleStart = 0
        val titleEnd = title.length

        val valueStart = title.length + 1
        val valueEnd = s.length

        // Title
        s.setSpan(ForegroundColorSpan(centerTitleColor), titleStart, titleEnd, 0)
        s.setSpan(RelativeSizeSpan(0.75f), titleStart, titleEnd, 0)
        s.setSpan(StyleSpan(Typeface.NORMAL), titleStart, titleEnd, 0)

        // Value
        s.setSpan(ForegroundColorSpan(color), valueStart, valueEnd, 0)
        s.setSpan(RelativeSizeSpan(1.0f), valueStart, valueEnd, 0)
        s.setSpan(StyleSpan(Typeface.BOLD), valueStart, valueEnd, 0)

        return s
    }

    Crossfade(
        modifier = modifier.padding(vertical = 8.dp),
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
                    setDrawEntryLabels(false)
                    isRotationEnabled = false
                    holeRadius = 75f
                    transparentCircleRadius = 79f
                    setTransparentCircleColor(android.graphics.Color.WHITE)
                    setTransparentCircleAlpha(50)
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
                    sliceSpace = 2f
                }

                val data = PieData(dataset)
                pieChart.centerText = createCenterText(title, totalAmount.formatThousand())

                pieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                    override fun onValueSelected(e: Entry?, h: Highlight?) {
                        if (e is PieEntry) {
                            val df = DecimalFormat("###,###,##0.0")
                            val total = data.yValueSum
                            val percentage = (e.value / total) * 100
                            pieChart.centerText = createCenterText(
                                e.label,
                                "${df.format(percentage)}%",
                            )
                        }
                    }

                    override fun onNothingSelected() {
                        pieChart.centerText = createCenterText(title, totalAmount.formatThousand())
                    }
                })

                pieChart.data = data
                pieChart.invalidate()
            }
        )
    }
}
