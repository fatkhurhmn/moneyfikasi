package dev.muffar.moneyfikasi.common_ui.component.calculator

sealed class CalculatorKey {
    data class Number(val value: String) : CalculatorKey()
    data object Dot : CalculatorKey()
    data class Operator(val operator: MathOperator) : CalculatorKey()
    data object Clear : CalculatorKey()
    data object Equals : CalculatorKey()
}