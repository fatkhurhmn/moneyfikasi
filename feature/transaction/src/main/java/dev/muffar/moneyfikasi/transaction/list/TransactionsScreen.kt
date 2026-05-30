package dev.muffar.moneyfikasi.transaction.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import dev.muffar.moneyfikasi.common_ui.component.EmptyDataList
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.ChooseDateSheet
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.CustomDateSheet
import dev.muffar.moneyfikasi.common_ui.component.calendar_header.DateRangeSwitcher
import dev.muffar.moneyfikasi.common_ui.component.transaction.TransactionsList
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.model.TransactionFilter
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsFilterSheet
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsLoading
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsTopBar
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDateTime
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    state: TransactionsState,
    onTransactionItemClick: (UUID, Boolean) -> Unit,
    onTimeReferenceChange: (LocalDateTime) -> Unit,
    onDateRangeChange: (DateRange) -> Unit,
    onShowFilterSheet: (Boolean) -> Unit,
    onShowChooseDateSheet: (Boolean) -> Unit,
    onShowCustomDateSheet: (Boolean) -> Unit,
    onSearchClick: () -> Unit,
    onResetFilter: () -> Unit,
    onFilterChanged: (TransactionFilter) -> Unit,
    onGetDailyBalance: (LocalDateTime) -> Flow<Double>,
) {
    val transactions = state.transactions.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TransactionsTopBar(
                onChooseDateClick = { onShowChooseDateSheet(true) },
                onSearchClick = onSearchClick,
                showFilterBadge = state.isFilterApplied,
                onFilterClick = { onShowFilterSheet(true) }
            )
        },
        contentWindowInsets = WindowInsets(0.dp),
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            DateRangeSwitcher(
                timeReference = state.timeReference,
                dateRange = state.dateRange,
                onTimeReferenceChange = onTimeReferenceChange,
            )

            if (transactions.loadState.refresh is LoadState.Loading) {
                TransactionsLoading()
            } else if (transactions.itemCount == 0) {
                EmptyDataList(
                    title = stringResource(id = R.string.empty_transactions_title),
                    description = stringResource(id = R.string.empty_transactions_msg),
                    bottomPadding = true
                )
            } else {
                TransactionsList(
                    transactions = transactions,
                    onItemClick = onTransactionItemClick,
                    onGetDailyBalance = onGetDailyBalance,
                    extraBottomSpace = true
                )
            }
        }

        AnimatedVisibility(state.showChooseDateSheet) {
            ChooseDateSheet(
                dateRange = state.dateRange,
                onDismissRequest = { onShowChooseDateSheet(false) },
                onCustomDateClick = { onShowCustomDateSheet(true) },
                onChoose = onDateRangeChange
            )
        }

        AnimatedVisibility(state.showCustomDateSheet) {
            CustomDateSheet(
                dateRange = state.dateRange,
                onDateChange = onDateRangeChange,
                onDismissRequest = { onShowCustomDateSheet(false) }
            )
        }

        AnimatedVisibility(state.showFilterSheet) {
            TransactionsFilterSheet(
                filter = state.filter,
                isFilterApplied = state.isFilterApplied,
                categories = state.categories,
                wallets = state.wallets,
                onApply = onFilterChanged,
                onResetFilter = onResetFilter,
                onDismissRequest = { onShowFilterSheet(false) }
            )
        }
    }
}
