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
import dev.muffar.moneyfikasi.common_ui.theme.color.MainColor

@Composable
fun CalculatorButton(
    key: CalculatorKey,
    onClick: () -> Unit
) {
    val (bgColor, textColor) = getButtonColors(key)

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

fun getButtonColors(action: CalculatorKey): Pair<Color, Color> {
    return when (action) {
        is CalculatorKey.Operation, CalculatorKey.ToggleSign -> Pair(
            MainColor.Yellow.dark,
            MainColor.White
        )

        is CalculatorKey.Clear -> Pair(MainColor.Red.kindaLight, MainColor.White)
        is CalculatorKey.Calculate -> Pair(MainColor.Green.primary, MainColor.White)
        is CalculatorKey.Delete -> Pair(MainColor.Blue.primary, MainColor.White)
        else -> Pair(MainColor.ExtraLightGray, MainColor.Black)
    }
}

private fun getSymbol(action: CalculatorKey): String {
    return when (action) {
        is CalculatorKey.Number -> action.number.toString()
        is CalculatorKey.Operation -> action.operation.symbol
        is CalculatorKey.TripleZero -> CalculatorSymbols.TRIPLE_ZERO
        CalculatorKey.Clear -> CalculatorSymbols.CLEAR
        CalculatorKey.Delete -> CalculatorSymbols.DELETE
        CalculatorKey.Decimal -> CalculatorSymbols.DECIMAL
        CalculatorKey.Calculate -> CalculatorSymbols.EQUALS
        CalculatorKey.ToggleSign -> CalculatorSymbols.TOGGLE_SIGN
    }
}
