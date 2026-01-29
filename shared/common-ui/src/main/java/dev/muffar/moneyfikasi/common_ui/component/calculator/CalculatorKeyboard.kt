package dev.muffar.moneyfikasi.common_ui.component.calculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun CalculatorKeyboard(
    onClick: (CalculatorKey) -> Unit
) {
    val buttons = listOf(
        CalculatorKey.Clear,
        CalculatorKey.ToggleSign,
        CalculatorKey.Delete,
        CalculatorKey.Operation(MathOperation.Divide),
        CalculatorKey.Number(7),
        CalculatorKey.Number(8),
        CalculatorKey.Number(9),
        CalculatorKey.Operation(MathOperation.Multiply),
        CalculatorKey.Number(4),
        CalculatorKey.Number(5),
        CalculatorKey.Number(6),
        CalculatorKey.Operation(MathOperation.Subtract),
        CalculatorKey.Number(1),
        CalculatorKey.Number(2),
        CalculatorKey.Number(3),
        CalculatorKey.Operation(MathOperation.Add),
        CalculatorKey.TripleZero,
        CalculatorKey.Number(0),
        CalculatorKey.Decimal,
        CalculatorKey.Calculate
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(buttons) { key ->
            CalculatorButton(
                key = key,
                onClick = { onClick(key) }
            )
        }
    }
}