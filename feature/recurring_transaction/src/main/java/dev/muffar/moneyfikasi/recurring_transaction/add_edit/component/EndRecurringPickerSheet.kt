package dev.muffar.moneyfikasi.recurring_transaction.add_edit.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Tag
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.BottomSheetTitle
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.PickerOptionItem
import dev.muffar.moneyfikasi.common_ui.component.button.DoubleOutlinedButton
import dev.muffar.moneyfikasi.domain.model.RecurringEndType
import dev.muffar.moneyfikasi.resource.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EndRecurringPickerSheet(
    modifier: Modifier = Modifier,
    selectedEndType: RecurringEndType,
    onEndTypeSelect: (RecurringEndType) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val endTypes = RecurringEndType.entries

    val hideSheet = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onDismissRequest()
            }
        }
    }

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        BottomSheetTitle(title = stringResource(R.string.end_recurring))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(endTypes) { endType ->
                val icon = when (endType) {
                    RecurringEndType.NEVER -> Icons.Rounded.Block
                    RecurringEndType.ON_DATE -> Icons.Rounded.CalendarToday
                    RecurringEndType.AFTER_OCCURRENCES -> Icons.Rounded.Tag
                }

                val title = when (endType) {
                    RecurringEndType.NEVER -> stringResource(R.string.never)
                    RecurringEndType.ON_DATE -> stringResource(R.string.on_date)
                    RecurringEndType.AFTER_OCCURRENCES -> stringResource(R.string.after_occurrences)
                }

                PickerOptionItem(
                    isSelected = endType == selectedEndType,
                    icon = icon,
                    title = title,
                    onClick = {
                        onEndTypeSelect(endType)
                        hideSheet()
                    }
                )
            }
        }

        CommonHorizontalDivider()
        DoubleOutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            leftText = stringResource(R.string.cancel),
            rightText = stringResource(R.string.reset),
            onLeftClick = { hideSheet() },
            onRightClick = {
                onEndTypeSelect(RecurringEndType.NEVER)
                hideSheet()
            }
        )
    }
}
