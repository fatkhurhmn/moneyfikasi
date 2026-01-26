package dev.muffar.moneyfikasi.common_ui.component.calculator

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.muffar.moneyfikasi.common_ui.theme.color.MainColor

@Composable
fun CalculatorOperatorButton(
    key: MathOperator,
    onClick: (MathOperator) -> Unit,
    modifier: Modifier = Modifier
) {
    CalculatorButton(
        text = key.symbol,
        onClick = { onClick(key) },
        modifier = modifier,
        backgroundColor = MainColor.Yellow.dark,
        textColor = MaterialTheme.colorScheme.onPrimary
    )
}