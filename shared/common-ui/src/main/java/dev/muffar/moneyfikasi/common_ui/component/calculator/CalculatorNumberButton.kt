package dev.muffar.moneyfikasi.common_ui.component.calculator

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CalculatorNumberButton(
    key: NumberKey,
    onClick: (NumberKey) -> Unit,
    modifier: Modifier
) {
    CalculatorButton(
        text = key.value,
        onClick = { onClick(key) },
        modifier = modifier
    )
}