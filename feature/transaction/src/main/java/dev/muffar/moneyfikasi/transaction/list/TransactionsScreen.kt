package dev.muffar.moneyfikasi.transaction.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.utils.TransactionFilter
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsBottomSheet
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsFilterSection
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsList
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsLoading
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsSheetType
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsTopBar
import java.util.UUID

@Composable
fun TransactionsScreen(
    modifier: Modifier = Modifier,
    state: TransactionsState,
    onTransactionItemClick: (UUID) -> Unit,
    onFilterChanged: (TransactionFilter) -> Unit,
    onDateRangeChange: (Long, Long) -> Unit,
    onShowBottomSheet: (TransactionsSheetType?) -> Unit,
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TransactionsTopBar(
                modifier = Modifier.padding(16.dp),
                onFilterClick = { onShowBottomSheet(TransactionsSheetType.FILTER) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets(0.dp)
    ) {
        Column(
            modifier = modifier.padding(it)
        ) {
            TransactionsFilterSection(
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
                    type = state.sheetType,
                    filter = state.filter,
                    startDateMillis = state.startDateRange,
                    endDateMillis = state.endDateRange,
                    onFilterChanged = onFilterChanged,
                    onDateChange = { start, date ->
                        onDateRangeChange(start, date)
                        onFilterChanged(TransactionFilter.CUSTOM)
                    },
                    onShowBottomSheet = onShowBottomSheet
                )
            }
        }
    }
}