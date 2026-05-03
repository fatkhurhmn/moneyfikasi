package dev.muffar.moneyfikasi.common_ui.theme.color

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class FinanceColors(
    val income: Color,
    val incomeContainer: Color,
    val onIncomeContainer: Color,
    val expense: Color,
    val expenseContainer: Color,
    val onExpenseContainer: Color,
    val budget: Color,
    val budgetContainer: Color,
    val onBudgetContainer: Color,
    val brand: Color,
    val brandContainer: Color,
    val onBrandContainer: Color,
    val brandKindaLight: Color,
    val brandDark: Color,
    val info: Color,
    val warning: Color,
    val chartColors: List<Color>,
)

val LocalFinanceColors = staticCompositionLocalOf {
    FinanceColors(
        income = Color.Unspecified,
        incomeContainer = Color.Unspecified,
        onIncomeContainer = Color.Unspecified,
        expense = Color.Unspecified,
        expenseContainer = Color.Unspecified,
        onExpenseContainer = Color.Unspecified,
        budget = Color.Unspecified,
        budgetContainer = Color.Unspecified,
        onBudgetContainer = Color.Unspecified,
        brand = Color.Unspecified,
        brandContainer = Color.Unspecified,
        onBrandContainer = Color.Unspecified,
        brandKindaLight = Color.Unspecified,
        brandDark = Color.Unspecified,
        info = Color.Unspecified,
        warning = Color.Unspecified,
        chartColors = emptyList(),
    )
}
