package dev.muffar.moneyfikasi.common_ui.component.calculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.theme.color.MainColor

@Composable
fun CalculatorKeyboard(
    onNumberClick: (NumberKey) -> Unit,
    onOperatorClick: (MathOperator) -> Unit,
    onDecimalClick: () -> Unit,
    onBackspaceClick: () -> Unit,
    onClearClick: () -> Unit,
    onEqualsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CalculatorRow {
            CalculatorButton(
                text = "C",
                onClick = onClearClick,
                modifier = Modifier.weight(1f),
                backgroundColor = MainColor.Red.kindaLight,
                textColor = MaterialTheme.colorScheme.onPrimary
            )
            CalculatorOperatorButton(
                key = MathOperator.DIVIDE,
                onClick = onOperatorClick,
                modifier = Modifier.weight(1f),
            )
            CalculatorOperatorButton(
                key = MathOperator.TIMES,
                onClick = onOperatorClick,
                modifier = Modifier.weight(1f),
            )
            CalculatorButton(
                text = "⌫",
                onClick = onBackspaceClick,
                modifier = Modifier.weight(1f),
                backgroundColor = MainColor.Blue.primary,
                textColor = MaterialTheme.colorScheme.onPrimary
            )
        }

        CalculatorRow {
            CalculatorNumberButton(
                key = NumberKey.ONE,
                onClick = onNumberClick,
                modifier = Modifier.weight(1f)
            )
            CalculatorNumberButton(
                key = NumberKey.TWO,
                onClick = onNumberClick,
                modifier = Modifier.weight(1f)
            )
            CalculatorNumberButton(
                key = NumberKey.THREE,
                onClick = onNumberClick,
                modifier = Modifier.weight(1f)
            )
            CalculatorOperatorButton(
                key = MathOperator.MINUS,
                onClick = onOperatorClick,
                modifier = Modifier.weight(1f),
            )
        }

        CalculatorRow {
            CalculatorNumberButton(
                key = NumberKey.FOUR,
                onClick = onNumberClick,
                modifier = Modifier.weight(1f)
            )
            CalculatorNumberButton(
                key = NumberKey.FIVE,
                onClick = onNumberClick,
                modifier = Modifier.weight(1f)
            )
            CalculatorNumberButton(
                key = NumberKey.SIX,
                onClick = onNumberClick,
                modifier = Modifier.weight(1f)
            )
            CalculatorOperatorButton(
                key = MathOperator.PLUS,
                onClick = onOperatorClick,
                modifier = Modifier.weight(1f),
            )
        }

        CalculatorRow {
            CalculatorNumberButton(
                key = NumberKey.SEVEN,
                onClick = onNumberClick,
                modifier = Modifier.weight(1f)
            )
            CalculatorNumberButton(
                key = NumberKey.EIGHT,
                onClick = onNumberClick,
                modifier = Modifier.weight(1f)
            )
            CalculatorNumberButton(
                key = NumberKey.NINE,
                onClick = onNumberClick,
                modifier = Modifier.weight(1f)
            )
            CalculatorOperatorButton(
                key = MathOperator.PERCENTAGE,
                onClick = onOperatorClick,
                modifier = Modifier.weight(1f),
            )
        }

        CalculatorRow {
            CalculatorNumberButton(
                key = NumberKey.TRIPLE_ZERO,
                onClick = onNumberClick,
                modifier = Modifier.weight(1f)
            )
            CalculatorNumberButton(
                key = NumberKey.ZERO,
                onClick = onNumberClick,
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                text = ".",
                onClick = onDecimalClick,
                modifier = Modifier.weight(1f)
            )
            CalculatorButton(
                text = "=",
                onClick = onEqualsClick,
                modifier = Modifier.weight(1f),
                backgroundColor = MainColor.Green.primary,
                textColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}