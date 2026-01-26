package dev.muffar.moneyfikasi.common_ui.component.calculator

enum class MathOperator(val symbol: String) {
    PLUS("+") {
        override fun apply(x: Double, y: Double): Double = x + y
    },
    MINUS("-") {
        override fun apply(x: Double, y: Double): Double = x - y
    },
    TIMES("×") {
        override fun apply(x: Double, y: Double): Double = x * y
    },
    DIVIDE("÷") {
        override fun apply(x: Double, y: Double): Double =
            if (y == 0.0) Double.NaN else x / y
    },

    PERCENTAGE("%") {
        override fun apply(x: Double, y: Double): Double =
            (x * y) / 100
    };

    abstract fun apply(x: Double, y: Double): Double

    companion object {
        private val SYMBOL_MAP = entries.associateBy { it.symbol }
        fun fromSymbol(symbol: String): MathOperator? = SYMBOL_MAP[symbol]
    }
}