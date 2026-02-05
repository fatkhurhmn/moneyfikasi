package dev.muffar.moneyfikasi.common_ui.component.calculator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CalculatorScreen(
    state: CalculatorState,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        CalculatorResultDisplay(
            input = state.input,
            history = state.history,
            error = state.error,
            modifier = Modifier.weight(1f)
        )
        CalculatorKeyboard(state::onAction)
    }
}