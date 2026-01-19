package dev.muffar.moneyfikasi.statistic.main.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.DateRangeSheet
import dev.muffar.moneyfikasi.domain.model.TimePeriod

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticBottomSheet(
    type: StatisticSheetType,
    filter: TimePeriod,
    startDateMillis: Long,
    endDateMillis: Long,
    onFilterChanged: (TimePeriod) -> Unit,
    onDateChange: (start: Long, end: Long) -> Unit,
    onShowBottomSheet: (StatisticSheetType?) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { onShowBottomSheet(null) }
    ) {
        when (type) {
            StatisticSheetType.FILTER -> StatisticFilterSheet(
                filter = filter,
                onFilterChanged = { filter ->
                    if (filter != TimePeriod.CUSTOM) {
                        onFilterChanged(filter)
                    } else {
                        onShowBottomSheet(StatisticSheetType.DATE)
                    }
                },
                onClose = { onShowBottomSheet(null) },
            )

            StatisticSheetType.DATE -> DateRangeSheet(
                startDateMillis = if (filter == TimePeriod.CUSTOM) startDateMillis else null,
                endDateMillis = if (filter == TimePeriod.CUSTOM) endDateMillis else null,
                onDateChange = onDateChange,
                onClose = { onShowBottomSheet(null) },
            )
        }
    }
}

enum class StatisticSheetType {
    FILTER,
    DATE
}