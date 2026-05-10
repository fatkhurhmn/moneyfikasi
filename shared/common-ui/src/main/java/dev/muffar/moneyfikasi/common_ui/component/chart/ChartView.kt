package dev.muffar.moneyfikasi.common_ui.component.chart

import android.graphics.drawable.GradientDrawable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.ColorUtils
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import dev.muffar.moneyfikasi.common_ui.component.chart.slider.SlideGestureListener
import dev.muffar.moneyfikasi.common_ui.component.chart.slider.SlidingMarkerView
import dev.muffar.moneyfikasi.common_ui.component.chart.slider.formatAmount
import dev.muffar.moneyfikasi.resource.R

@Composable
fun ChartView(
    chartData: ChartData,
    incomeColor: Int,
    expenseColor: Int,
    textColor: Int,
    surfaceColor: Int,
    modifier: Modifier = Modifier,
) {
    val incomeLabel = stringResource(R.string.income)
    val expenseLabel = stringResource(R.string.expense)
    AndroidView(
        modifier = modifier,
        factory = { context ->
            LineChart(context).apply {
                setupStaticConfig(textColor)
            }
        },
        update = { chart ->
            // Sync theme colors
            chart.legend.textColor = textColor
            chart.xAxis.textColor = textColor
            chart.axisLeft.textColor = textColor
            chart.axisLeft.gridColor = ColorUtils.setAlphaComponent(textColor, 100)

            // Attach fresh MarkerView so it always has up-to-date chartData
            chart.marker = SlidingMarkerView(
                context = chart.context,
                chartData = chartData,
                textColor = textColor,
                bgColor = surfaceColor,
                borderColor = ColorUtils.setAlphaComponent(textColor, 100),
                incomeColor = incomeColor,
                expenseColor = expenseColor,
            ).also { it.chartView = chart }

            // Build datasets
            chart.data = LineData(
                createDataSet(
                    values = chartData.expenseValues,
                    label = expenseLabel,
                    color = expenseColor
                ),
                createDataSet(
                    values = chartData.incomeValues,
                    label = incomeLabel,
                    color = incomeColor
                ),
            )

            // X labels
            chart.xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val i = value.toInt()
                    return if (i in chartData.labels.indices) chartData.labels[i] else ""
                }
            }

            // Slide gesture: update highlight on every finger move
            chart.onChartGestureListener = SlideGestureListener(chart)

            chart.animateX(500)
            chart.invalidate()
        },
    )
}

fun LineChart.setupStaticConfig(textColor: Int) {
    description.isEnabled = false
    setPinchZoom(false)
    setScaleEnabled(false)
    setDrawGridBackground(false)
    setDrawBorders(false)
    setTouchEnabled(true)
    isDragXEnabled = true
    isDragYEnabled = false
    isHighlightPerDragEnabled = true
    isHighlightPerTapEnabled = true

    extraBottomOffset = 12f
    extraTopOffset = 8f

    legend.isEnabled = false

    xAxis.apply {
        position = XAxis.XAxisPosition.BOTTOM
        setDrawGridLines(false)
        setDrawAxisLine(false)
        granularity = 1f
        isGranularityEnabled = true
        this.textColor = textColor
        yOffset = 8f
    }

    axisLeft.apply {
        setDrawGridLines(true)
        setDrawAxisLine(false)
        enableGridDashedLine(10f, 10f, 0f)
        spaceTop = 20f
        spaceBottom = 10f
        this.textColor = textColor
        valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float) = formatAmount(value.toDouble())
        }
    }

    axisRight.isEnabled = false
}

fun createDataSet(
    values: List<Double>,
    label: String,
    color: Int,
): LineDataSet {
    val entries = values.mapIndexed { i, v -> Entry(i.toFloat(), v.toFloat()) }
    return LineDataSet(entries, label).apply {
        this.color = color
        setDrawValues(false)
        setDrawCircles(false)
        setDrawHighlightIndicators(true)
        highLightColor = color
        highlightLineWidth = 1.5f
        lineWidth = 2.5f
        mode = LineDataSet.Mode.CUBIC_BEZIER
        cubicIntensity = 0.2f

        setDrawFilled(true)
        fillDrawable = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(
                ColorUtils.setAlphaComponent(color, 50),
                android.graphics.Color.TRANSPARENT,
            ),
        )
    }
}