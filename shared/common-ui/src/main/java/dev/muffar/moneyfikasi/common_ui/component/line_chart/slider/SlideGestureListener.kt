package dev.muffar.moneyfikasi.common_ui.component.line_chart.slider

import android.view.MotionEvent
import android.view.View
import com.github.mikephil.charting.charts.LineChart
import kotlin.math.abs

class SlideGestureListener(
    var onSliding: (Boolean) -> Unit,
) : View.OnTouchListener {

    var chart: LineChart? = null
    private var isSliding = false
    private var startX = 0f
    private var startY = 0f
    private val slop = 10f

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val chart = chart ?: return false

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
            }

            MotionEvent.ACTION_MOVE -> {
                if (!isSliding) {
                    val dx = abs(event.x - startX)
                    val dy = abs(event.y - startY)
                    // If horizontal movement is significant, start sliding
                    if (dx > slop && dx > dy) {
                        isSliding = true
                        onSliding(true)
                        // Inform parent to not intercept this gesture
                        v.parent.requestDisallowInterceptTouchEvent(true)
                    }
                }

                if (isSliding) {
                    val h = chart.getHighlightByTouchPoint(event.x, event.y)
                    chart.highlightValue(h, true)
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (isSliding) {
                    isSliding = false
                    onSliding(false)
                    chart.highlightValue(null, true)
                } else if (event.action == MotionEvent.ACTION_UP) {
                    // Fix for: SlideGestureListener#onTouch should call View#performClick
                    v.performClick()
                }
            }
        }
        // Consume events if sliding, so chart doesn't do its own drag processing
        return isSliding
    }
}
