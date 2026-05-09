package dev.muffar.moneyfikasi.common_ui.component.chart.slider

import android.view.MotionEvent
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import kotlin.math.abs

class SlideGestureListener(private val chart: LineChart) : OnChartGestureListener {

    override fun onChartGestureStart(me: MotionEvent, last: ChartTouchListener.ChartGesture) =
        updateHighlight(me)

    override fun onChartSingleTapped(me: MotionEvent) =
        updateHighlight(me)

    override fun onChartLongPressed(me: MotionEvent) =
        updateHighlight(me)

    // Called on every pixel of finger movement — this is what enables sliding
    override fun onChartTranslate(me: MotionEvent, dX: Float, dY: Float) {
        if (abs(dX) > abs(dY)) {
            updateHighlight(me)
        }
    }

    override fun onChartGestureEnd(me: MotionEvent, last: ChartTouchListener.ChartGesture) {
        // Intentionally keep the last highlight visible when the finger lifts,
        // consistent with standard MPAndroidChart behaviour.
        chart.highlightValue(null, true)
    }

    override fun onChartDoubleTapped(me: MotionEvent) {}
    override fun onChartFling(me1: MotionEvent, me2: MotionEvent, vX: Float, vY: Float) {}
    override fun onChartScale(me: MotionEvent, scaleX: Float, scaleY: Float) {}

    private fun updateHighlight(me: MotionEvent) {
        val h = chart.getHighlightByTouchPoint(me.x, me.y) ?: return
        chart.highlightValue(h, true)
    }
}
