package dev.muffar.moneyfikasi.transaction.list

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.List
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.ExpandableFloatingActionButton
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.utils.TransactionFilter
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsBottomSheet
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsDateFilterSection
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsList
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsLoading
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsSheetType
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsTopBar
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    modifier: Modifier = Modifier,
    state: TransactionsState,
    onTransactionItemClick: (UUID) -> Unit,
    onExpandFabButton: (Boolean) -> Unit,
    onNavigateToAddScreen: (TransactionType) -> Unit,
    onFilterChanged: (TransactionFilter) -> Unit,
    onDateRangeChange: (Long, Long) -> Unit,
    onShowBottomSheet: (TransactionsSheetType?) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = state.sheetType == TransactionsSheetType.DATE
    )

    Scaffold(
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(
                onTap = {
                    onExpandFabButton(false)
                }
            )
        },
        topBar = {
            TransactionsTopBar(
                modifier = Modifier.padding(16.dp),
                onFilterClick = { onShowBottomSheet(TransactionsSheetType.FILTER) }
            )
        },
        contentWindowInsets = WindowInsets(0.dp),
        floatingActionButton = {
            ExpandableFloatingActionButton(
                isExpanded = state.isExpandedFab,
                onClick = { onExpandFabButton(!state.isExpandedFab) },
                fabIcon = Icons.Rounded.Add,
                onTransactionClick = {
                    onNavigateToAddScreen(it)
                    onExpandFabButton(false)
                }
            )
        }
    ) {
        Column(
            modifier = modifier.padding(it)
        ) {
            TransactionsDateFilterSection(
                filter = state.filter,
                startDateMillis = state.startDateRange,
                endDateMillis = state.endDateRange,
                onDateChange = onDateRangeChange
            )

            if (state.transactionsByDate.isNotEmpty()) {
                val dates = state.transactionsByDate.keys.toList()
                val transactions = state.transactionsByDate.values.toList()

                TransactionsList(
                    dates = dates,
                    transactions = transactions,
                    onItemClick = onTransactionItemClick
                )
            } else {
                if (state.isLoading) {
                    TransactionsLoading()
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.TwoTone.List,
                            contentDescription = stringResource(R.string.no_transactions),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                            modifier = Modifier.size(100.dp)
                        )
                        Text(text = stringResource(R.string.no_transactions))
                    }
                }
            }

            if (state.sheetType != null) {
                TransactionsBottomSheet(
                    state = sheetState,
                    type = state.sheetType,
                    filter = state.filter,
                    startDateMillis = state.startDateRange,
                    endDateMillis = state.endDateRange,
                    onFilterChanged = onFilterChanged,
                    onDateChange = { start, date ->
                        onFilterChanged(TransactionFilter.CUSTOM)
                        onDateRangeChange(start, date)
                    },
                    onShowBottomSheet = onShowBottomSheet
                )
            }
        }
    }
}