package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import dev.muffar.moneyfikasi.domain.utils.TransactionFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsBottomSheet(
    state : SheetState,
    type: TransactionsSheetType,
    filter: TransactionFilter,
    startDateMillis: Long,
    endDateMillis: Long,
    onFilterChanged: (TransactionFilter) -> Unit,
    onDateChange: (start: Long, end: Long) -> Unit,
    onShowBottomSheet: (TransactionsSheetType?) -> Unit,
) {
    ModalBottomSheet(
        sheetState = state,
        onDismissRequest = { onShowBottomSheet(null) }
    ) {
        when (type) {
            TransactionsSheetType.FILTER -> TransactionsFilterSheet(
                filter = filter,
                onFilterChanged = { filter ->
                    if (filter != TransactionFilter.CUSTOM) {
                        onFilterChanged(filter)
                    } else {
                        onShowBottomSheet(TransactionsSheetType.DATE)
                    }
                },
                onClose = { onShowBottomSheet(null) },
            )

            TransactionsSheetType.DATE -> DateRangeSheet(
                startDateMillis = if (filter == TransactionFilter.CUSTOM) startDateMillis else null,
                endDateMillis = if (filter == TransactionFilter.CUSTOM) endDateMillis else null,
                onDateChange = onDateChange,
                onClose = { onShowBottomSheet(null) },
            )
        }
    }
}

enum class TransactionsSheetType {
    FILTER,
    DATE
}