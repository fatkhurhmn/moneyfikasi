package dev.muffar.moneyfikasi.common_ui.component.line_chart.slider

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.TypedValue
import androidx.core.graphics.ColorUtils
import androidx.core.os.ConfigurationCompat
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import dev.muffar.moneyfikasi.common_ui.component.line_chart.ChartData
import dev.muffar.moneyfikasi.resource.R
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs
import kotlin.math.roundToInt

@SuppressLint("ViewConstructor")
class SlidingMarkerView(
    context: Context,
    private val chartData: ChartData,
    private val textColor: Int,
    private val bgColor: Int,
    private val borderColor: Int,
    private val incomeColor: Int,
    private val expenseColor: Int,
) : MarkerView(context, android.R.layout.simple_list_item_1) {

    private val dp = context.resources.displayMetrics.density
    private val pad = 10f * dp
    private val lh = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        18f,
        context.resources.displayMetrics
    )
    private val r = 8f * dp

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val txPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            11f,
            context.resources.displayMetrics
        )
        typeface = Typeface.DEFAULT_BOLD
    }

    // Populated in refreshContent(), read in draw()
    private var rows = emptyList<Pair<String, Int>>() // text → color

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        val idx = e?.x?.toInt() ?: run { super.refreshContent(e, highlight); return }
        if (idx !in chartData.labels.indices) {
            super.refreshContent(e, highlight); return
        }

        rows = listOf(
            chartData.labels[idx] to ColorUtils.setAlphaComponent(textColor, 160),
            "${context.getString(R.string.label_income)}   ${formatAmount(context, chartData.incomeValues[idx])}" to incomeColor,
            "${context.getString(R.string.label_expense)}  ${formatAmount(context, chartData.expenseValues[idx])}" to expenseColor,
        )
        super.refreshContent(e, highlight)
    }

    override fun draw(canvas: Canvas, posX: Float, posY: Float) {
        if (rows.isEmpty()) return

        val bw = rows.maxOf { txPaint.measureText(it.first) } + pad * 2
        val bh = rows.size * lh + pad * 2

        val chartW = chartView?.width?.toFloat() ?: 0f
        val left = (posX - bw / 2f).coerceIn(0f, (chartW - bw).coerceAtLeast(0f))
        val top = 0f

        // Drop shadow
        bgPaint.color = ColorUtils.setAlphaComponent(android.graphics.Color.BLACK, 18)
        bgPaint.style = Paint.Style.FILL
        canvas.drawRoundRect(left + 2f, top + 2f, left + bw + 2f, top + bh + 2f, r, r, bgPaint)

        // Background
        bgPaint.color = bgColor
        canvas.drawRoundRect(left, top, left + bw, top + bh, r, r, bgPaint)

        // Border
        bgPaint.color = borderColor
        bgPaint.style = Paint.Style.STROKE
        bgPaint.strokeWidth = 1f
        canvas.drawRoundRect(left, top, left + bw, top + bh, r, r, bgPaint)
        bgPaint.style = Paint.Style.FILL

        // Text rows
        rows.forEachIndexed { i, (text, color) ->
            txPaint.color = color
            canvas.drawText(text, left + pad, top + pad + (i + 1) * lh - lh * 0.15f, txPaint)
        }
    }

    // We handle positioning ourselves, so return zero offset
    override fun getOffset(): MPPointF = MPPointF(0f, 0f)
}

fun formatAmount(context: Context, value: Double): String {
    val locale = ConfigurationCompat.getLocales(context.resources.configuration)[0]
        ?: Locale.getDefault()
    val thousandSuffix = context.getString(R.string.format_amount_thousand_suffix)
    val millionSuffix = context.getString(R.string.format_amount_million_suffix)
    val billionSuffix = context.getString(R.string.format_amount_billion_suffix)

    val a = abs(value)
    val sign = if (value < 0) "-" else ""
    return when {
        a >= 1_000_000_000 -> "$sign${
            String.format(
                locale,
                "%.1f%s",
                a / 1_000_000_000,
                billionSuffix
            )
        }"

        a >= 1_000_000 -> "$sign${String.format(locale, "%.1f%s", a / 1_000_000, millionSuffix)}"
        a >= 1_000 -> "$sign${String.format(locale, "%.1f%s", a / 1_000, thousandSuffix)}"
        else -> "$sign${NumberFormat.getIntegerInstance(locale).format(a.roundToInt())}"
    }
}