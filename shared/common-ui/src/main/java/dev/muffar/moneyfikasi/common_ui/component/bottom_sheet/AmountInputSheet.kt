package dev.muffar.moneyfikasi.common_ui.component.bottom_sheet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.common_ui.component.button.RowNegativePositiveButton
import dev.muffar.moneyfikasi.common_ui.component.calculator.CalculatorScreen
import dev.muffar.moneyfikasi.common_ui.component.calculator.rememberCalculatorState
import dev.muffar.moneyfikasi.resource.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmountInputSheet(
    onConfirm: (String) -> Unit,
    onDismissRequest: () -> Unit,
) {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val hideSheet = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onDismissRequest()
            }
        }
    }

    ModalBottomSheet(
        modifier = Modifier.statusBarsPadding(),
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        val calcState = rememberCalculatorState()
        BottomSheetTitle(stringResource(R.string.enter_amount))
        CommonHorizontalDivider()
        CalculatorScreen(
            state = calcState,
            modifier = Modifier.weight(1f)
        )
        CommonHorizontalDivider()
        RowNegativePositiveButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            negativeText = stringResource(R.string.cancel),
            positiveText = stringResource(R.string.ok),
            positiveEnabled = calcState.errorMessage.isEmpty(),
            onNegativeClick = {
                hideSheet()
                onDismissRequest()
            },
            onPositiveClick = {
                hideSheet()
                onConfirm(calcState.input)
            }
        )
    }
}