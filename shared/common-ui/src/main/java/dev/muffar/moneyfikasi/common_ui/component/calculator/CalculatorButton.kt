package dev.muffar.moneyfikasi.common_ui.component.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.common_ui.theme.color.MainColor

@Composable
fun CalculatorButton(
    key: CalculatorKey,
    onClick: () -> Unit
) {
    val financeColors = MoneyfikasiTheme.financeColors
    val (bgColor, textColor) = getButtonColors(key, financeColors)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(60.dp)
            .clip(MaterialTheme.shapes.large)
            .background(bgColor)
            .clickable { onClick() }
    ) {
        Text(
            text = getSymbol(key),
            style = MaterialTheme.typography.titleLarge,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}

fun getButtonColors(
    action: CalculatorKey,
    financeColors: dev.muffar.moneyfikasi.common_ui.theme.color.FinanceColors,
): Pair<Color, Color> {
    return when (action) {
        is CalculatorKey.Operation, CalculatorKey.ToggleSign -> Pair(
            MainColor.YellowDark,
            MainColor.White
        )

        is CalculatorKey.Clear -> Pair(financeColors.expenseContainer, Color.White)
        is CalculatorKey.Calculate -> Pair(financeColors.income, Color.White)
        is CalculatorKey.Delete -> Pair(financeColors.info, Color.White)
        else -> Pair(MainColor.ExtraLightGray, Color.Black)
    }
}

private fun getSymbol(action: CalculatorKey): String {
    return when (action) {
        is CalculatorKey.Number -> action.number.toString()
        is CalculatorKey.Operation -> action.operation.symbol
        is CalculatorKey.TripleZero -> CalculatorSymbols.TRIPLE_ZERO
        is CalculatorKey.DoubleZero -> CalculatorSymbols.DOUBLE_ZERO
        CalculatorKey.Clear -> CalculatorSymbols.CLEAR
        CalculatorKey.Delete -> CalculatorSymbols.DELETE
        CalculatorKey.Calculate -> CalculatorSymbols.EQUALS
        CalculatorKey.ToggleSign -> CalculatorSymbols.TOGGLE_SIGN
    }
}
