package dev.muffar.moneyfikasi.common_ui.component.bottom_sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AccessTimeFilled
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.common_ui.component.button.RowNegativePositiveButton
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.resource.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerSheet(
    time: Pair<Int, Int>,
    onDismissRequest: () -> Unit,
    onTimeSelect: (Pair<Int, Int>) -> Unit,
) {
    val (hour, minute) = time
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val pickerState = rememberTimePickerState(
                initialHour = hour,
                initialMinute = minute,
                is24Hour = true
            )

            val colors = TimePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface,
                clockDialColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.8f),
                timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.8f),
                timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary,
                timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurface,
                timeSelectorSelectedContentColor = MaterialTheme.colorScheme.onPrimary,
            )
            var showDial by remember { mutableStateOf(true) }

            BottomSheetTitle(stringResource(R.string.select_time))
            Spacer(Modifier.height(16.dp))

            if (showDial) {
                TimePicker(
                    state = pickerState,
                    colors = colors,
                )
            } else {
                TimeInput(
                    state = pickerState,
                    colors = colors.copy(
                        timeSelectorSelectedContainerColor = MoneyfikasiTheme.financeColors.brandContainer,
                        timeSelectorSelectedContentColor = MaterialTheme.colorScheme.onSurface
                    ),
                )
            }

            TimePickerModeButton(
                isDialMode = showDial,
                onClick = { showDial = !showDial }
            )

            CommonHorizontalDivider()
            RowNegativePositiveButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                negativeText = stringResource(R.string.cancel),
                positiveText = stringResource(R.string.select),
                onNegativeClick = {
                    hideSheet()
                    onDismissRequest()
                },
                onPositiveClick = {
                    hideSheet()
                    onTimeSelect(pickerState.hour to pickerState.minute)
                }
            )
        }
    }
}

@Composable
private fun TimePickerModeButton(
    isDialMode: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        IconButton(
            onClick
        ) {
            Icon(
                imageVector = if (!isDialMode) Icons.TwoTone.AccessTimeFilled else Icons.TwoTone.Edit,
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}