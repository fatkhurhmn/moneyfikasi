package dev.muffar.moneyfikasi.common_ui.component.line_chart

data class ChartData(
    val labels: List<String>,
    val incomeValues: List<Double>,
    val expenseValues: List<Double>,
)