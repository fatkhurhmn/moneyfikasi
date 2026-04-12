package dev.muffar.moneyfikasi.common_ui.component.calculator

sealed class CalculatorKey {
    data class Number(val number: Int) : CalculatorKey()
    object TripleZero : CalculatorKey()
    object DoubleZero : CalculatorKey()
    object Clear : CalculatorKey()
    object Delete : CalculatorKey()
    object Calculate : CalculatorKey()
    object ToggleSign : CalculatorKey()
    data class Operation(val operation: MathOperation) : CalculatorKey()
}