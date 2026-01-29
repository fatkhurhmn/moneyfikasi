package dev.muffar.moneyfikasi.common_ui.component.calculator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.text.DecimalFormat

@Composable
fun rememberCalculatorState() = remember { CalculatorState() }

class CalculatorState {
    var input by mutableStateOf("0")
        private set

    var history by mutableStateOf("")
        private set

    fun onAction(action: CalculatorKey) {
        when (action) {
            is CalculatorKey.Number -> appendInput(action.number.toString())
            is CalculatorKey.TripleZero -> appendInput(CalculatorSymbols.TRIPLE_ZERO)
            is CalculatorKey.Decimal -> {
                if (!canAddDecimal(input)) return
                appendInput(CalculatorSymbols.DECIMAL)
            }

            is CalculatorKey.Operation -> appendOperation(action.operation.symbol)
            is CalculatorKey.Clear -> {
                input = "0"
                history = ""
            }

            is CalculatorKey.Delete -> performDeletion()
            is CalculatorKey.Calculate -> performCalculation()
            is CalculatorKey.ToggleSign -> performSignToggle()
        }
    }

    private fun appendInput(value: String) {
        if (input == "Error") {
            input = if (value == CalculatorSymbols.DECIMAL) "0." else value
            return
        }

        val lastSegment = input.takeLastWhile { !isOperator(it) }
        if ((lastSegment + value).count { it.isDigit() } > 17 && value.any { it.isDigit() }) {
            return
        }

        if (input == "0" && value != CalculatorSymbols.DECIMAL) {
            input = value
        } else {
            input += value
        }
    }

    private fun appendOperation(symbol: String) {
        val ops = MathOperation.entries.map { it.symbol } // ["+", "-", "×", "÷"]

        if (input.isNotEmpty() && input.last().toString() in ops) {
            input = input.dropLast(1) + symbol
        } else {
            input += symbol
        }
    }

    private fun performDeletion() {
        input = if (input.length > 1) {
            input.dropLast(1)
        } else {
            "0"
        }
    }

    private fun performSignToggle() {
        if (input == "0") return

        val regex = Regex("(?<!\\d)(-?\\d+\\.?\\d*)$")
        val match = regex.find(input) ?: return

        val number = match.value
        val range = match.range

        val beforeIndex = range.first - 1
        val beforeChar = input.getOrNull(beforeIndex)

        if (!number.startsWith("-") && (beforeChar == '+' || beforeChar == '-')) {
            val flippedOp = if (beforeChar == '+') '-' else '+'
            input = input.replaceRange(beforeIndex, beforeIndex + 1, flippedOp.toString())
            return
        }

        val toggled = if (number.startsWith("-")) {
            number.removePrefix("-")
        } else {
            "-$number"
        }

        input = input.replaceRange(range, toggled)
    }

    private fun performCalculation() {
        try {
            if (input.isNotEmpty() && input.last().isDigit()) {
                val result = evaluateExpression(input)
                history = input
                input = formatResult(result)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            input = "Error"
        }
    }

    private fun formatResult(value: Double): String {
        val df = DecimalFormat("#.########")
        val formatted = df.format(value)
        if (formatted.count { it.isDigit() } > 17) {
            return "Error"
        }
        return formatted
    }

    private fun evaluateExpression(expression: String): Double {
        val tokens = parseTokens(expression)
        if (tokens.isEmpty()) return 0.0

        val intermediate = mutableListOf<Any>()
        var i = 0
        while (i < tokens.size) {
            val token = tokens[i]
            if (token is String && (token == MathOperation.Multiply.symbol || token == MathOperation.Divide.symbol)) {
                val left = intermediate.removeAt(intermediate.lastIndex) as Double
                val right = tokens[i + 1] as Double

                val result =
                    if (token == MathOperation.Multiply.symbol) left * right else left / right

                intermediate.add(result)
                i += 2
            } else {
                intermediate.add(token)
                i++
            }
        }

        var result = intermediate[0] as Double
        var j = 1
        while (j < intermediate.size) {
            val operator = intermediate[j] as String
            val nextVal = intermediate[j + 1] as Double
            if (operator == MathOperation.Add.symbol) result += nextVal
            else if (operator == MathOperation.Subtract.symbol) result -= nextVal
            j += 2
        }
        return result
    }

    private fun parseTokens(expression: String): List<Any> {
        val tokens = mutableListOf<Any>()
        val currentNumber = StringBuilder()

        for ((index, char) in expression.withIndex()) {
            if (isOperator(char)) {
                val isUnaryMinus = char == '-' && (index == 0 || isOperator(expression[index - 1]))

                if (isUnaryMinus) {
                    currentNumber.append(char)
                } else {
                    if (currentNumber.isNotEmpty()) {
                        tokens.add(currentNumber.toString().toDouble())
                        currentNumber.clear()
                    }
                    tokens.add(char.toString())
                }
            } else {
                currentNumber.append(char)
            }
        }
        if (currentNumber.isNotEmpty()) {
            tokens.add(currentNumber.toString().toDouble())
        }
        return tokens
    }

    private fun canAddDecimal(currentInput: String): Boolean {
        val lastSegment = currentInput.takeLastWhile { !isOperator(it) }
        return !lastSegment.contains(".")
    }

    private fun isOperator(char: Char): Boolean {
        return char.toString() in MathOperation.entries.map { it.symbol }.toSet()
    }
}
