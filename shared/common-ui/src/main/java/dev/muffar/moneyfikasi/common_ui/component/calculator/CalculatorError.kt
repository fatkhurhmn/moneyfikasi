package dev.muffar.moneyfikasi.common_ui.component.calculator

sealed class CalculatorError {
    object TooLargeNumber : CalculatorError()
    object InvalidExpression : CalculatorError()
    object InvalidResultFormat : CalculatorError()
}
