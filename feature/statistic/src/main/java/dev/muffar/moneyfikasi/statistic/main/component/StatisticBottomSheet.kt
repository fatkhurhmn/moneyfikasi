package dev.muffar.moneyfikasi.statistic.main.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import dev.muffar.moneyfikasi.common_ui.component.DateRangeSheet
import dev.muffar.moneyfikasi.domain.utils.TransactionDateFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticBottomSheet(
    type: StatisticSheetType,
    filter: TransactionDateFilter,
    startDateMillis: Long,
    endDateMillis: Long,
    onFilterChanged: (TransactionDateFilter) -> Unit,
    onDateChange: (start: Long, end: Long) -> Unit,
    onShowBottomSheet: (StatisticSheetType?) -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = { onShowBottomSheet(null) }
    ) {
        when (type) {
            StatisticSheetType.FILTER -> StatisticFilterSheet(
                filter = filter,
                onFilterChanged = { filter ->
                    if (filter != TransactionDateFilter.CUSTOM) {
                        onFilterChanged(filter)
                    } else {
                        onShowBottomSheet(StatisticSheetType.DATE)
                    }
                },
                onClose = { onShowBottomSheet(null) },
            )

            StatisticSheetType.DATE -> DateRangeSheet(
                startDateMillis = if (filter == TransactionDateFilter.CUSTOM) startDateMillis else null,
                endDateMillis = if (filter == TransactionDateFilter.CUSTOM) endDateMillis else null,
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